package es.uniovi.miw.monitora.agent.snapshot;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import javax.sql.rowset.WebRowSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.rowset.WebRowSetImpl;

import es.uniovi.miw.monitora.agent.task.quartz.ConsultaJob;
import es.uniovi.miw.monitora.core.utils.ZipUtils;
import es.uniovi.miw.monitora.server.conf.Conf;
import es.uniovi.miw.monitora.server.conf.PersistenceFactory;
import es.uniovi.miw.monitora.server.conf.ServicesFactory;
import es.uniovi.miw.monitora.server.model.Consulta;
import es.uniovi.miw.monitora.server.model.Destino;
import es.uniovi.miw.monitora.server.model.Informe;
import es.uniovi.miw.monitora.server.model.Snapshot;
import es.uniovi.miw.monitora.server.model.exceptions.BusinessException;
import es.uniovi.miw.monitora.server.persistence.util.PersistenceService;

public class SnapshotManager {
	static Logger logger = LoggerFactory.getLogger(SnapshotManager.class);

	private static SnapshotManager instance;
	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMMdd");
	private PersistenceService db;
	private Path base = Paths.get(Conf.get("snapshot.path"));

	private Map<String, Snapshot> snapshots = new HashMap<String, Snapshot>();
	private Map<Snapshot, Set<String>> results = new HashMap<Snapshot, Set<String>>();
	private Map<Snapshot, String> dumps = new HashMap<Snapshot, String>();

	public static SnapshotManager getInstance() throws BusinessException {
		if (instance == null) {
			instance = new SnapshotManager();
		}

		return instance;
	}

	protected SnapshotManager() throws BusinessException {
		db = PersistenceFactory.getPersistenceService();
	}

	protected synchronized Snapshot createSnapshot(Integer idCliente,
			Integer idDestino, Integer idInforme) throws BusinessException {

		Destino des = ServicesFactory.getDestinoService().findDestinoById(
				idDestino, idCliente);
		Informe info = ServicesFactory.getInformeService().findInformeById(
				idInforme);
		Snapshot snapshot = ServicesFactory.getSnapshotService()
				.createSnapshot(des, info, Calendar.getInstance().getTime());

		ServicesFactory.getSnapshotService().addSnapshot(snapshot);

		return snapshot;
	}

	protected synchronized URI dumpSnapshot(Snapshot snapshot, String tabla)
			throws BusinessException {
		try {

			Path snapshotDir = base.resolve(getSnapshotDirName(snapshot));
			Path metadataFile = snapshotDir.resolve("metadata.properties");

			if (!Files.isDirectory(snapshotDir)) {
				createSnapshoDir(snapshot, snapshotDir, metadataFile);
			}

			Properties prop = new Properties();
			FileReader fr = new FileReader(metadataFile.toFile());
			prop.load(fr);
			fr.close();

			Path snapshotFile = Files.createFile(snapshotDir
					.resolve(getSnapshotFileName(snapshot, tabla) + ".xml"));

			prop.setProperty("snapshot." + snapshot.getId().getIdSnapshot()
					+ ".file", snapshotDir.relativize(snapshotFile).toString());
			prop.setProperty("snapshot." + snapshot.getId().getIdSnapshot()
					+ ".URI", snapshotFile.toAbsolutePath().toUri().toString());
			prop.setProperty("snapshot." + snapshot.getId().getIdSnapshot()
					+ ".tabla", tabla);
			FileWriter fw = new FileWriter(metadataFile.toFile());
			prop.store(fw, null);
			fw.close();

			db.dumpToXML(tabla, snapshotFile);

			return snapshotFile.toAbsolutePath().toUri();

			// FIXME delete DB
		} catch (IOException e) {
			throw new BusinessException(e);
		}
	}

	private void createSnapshoDir(Snapshot snapshot, Path snapshotDir,
			Path metadataFile) throws IOException {
		Properties prop = new Properties();
		Files.createDirectory(snapshotDir);
		Files.createFile(metadataFile);
		FileWriter fw = new FileWriter(metadataFile.toFile());

		prop.setProperty("cliente", snapshot.getId().getIdCliente().toString());
		prop.setProperty("destino", snapshot.getId().getIdDestino().toString());
		prop.setProperty("informe", snapshot.getInforme().getInfoId()
				.toString());
		prop.setProperty("basePath", snapshotDir.toUri().toString());
		prop.store(fw, null);
		fw.close();
	}

	protected String getSnapshotFileName(Snapshot snapshot, String tabla) {
		return MessageFormat.format("table-{1}-{2}", tabla,
				dateFormat.format(snapshot.getFecha()));
	}

	// TODO deleteme
	protected String getSnapshotDirName(Snapshot snapshot) {
		return MessageFormat.format("snapshot-{0}-{1}-{2}", snapshot.getId()
				.getIdCliente(), snapshot.getId().getIdDestino(), snapshot
				.getId().getIdSnapshot());
	}

	public void add(ConsultaJob jobResult) throws BusinessException {
		Snapshot snap;
		String key = jobResult.getIdCliente() + "-" + jobResult.getIdDestino();
		snap = snapshots.get(key);
		if (snap == null) {
			snap = createSnapshot(jobResult.getIdCliente(),
					jobResult.getIdDestino(), jobResult.getIdInforme());
			snapshots.put(key, snap);
			results.put(snap, new HashSet<String>());
		}

		results.get(snap).add(jobResult.getTabla());
	}

	public Map<Snapshot, String> dump() throws BusinessException {
		dumps.clear();
		for (Entry<Snapshot, Set<String>> resultBySnapshot : results.entrySet()) {
			Snapshot snap = resultBySnapshot.getKey();
			for (String tabla : resultBySnapshot.getValue()) {
				dumpSnapshot(snap, tabla);
			}

			Path dir = base.resolve(getSnapshotDirName(snap));
			Path zfile = base.resolve(getSnapshotDirName(snap) + ".zip");
			try {
				ZipUtils.zipFolder(dir.toFile(), zfile.toFile());
			} catch (IOException e) {
				throw new BusinessException(e);
			}

			dumps.put(snap, zfile.toString());
		}
		snapshots.clear();
		results.clear();
		return dumps;
	}
}
Monitora Agent
=========================

Agent component for an Oracle DB monitorization tool.

Requisites
----------
*  Eclipse Kepler
*  Jersey-client 2.6
*  Maven


Installation
------------
1.  Clone repo
2.  Import in eclipse
3.  Maven install to get dependencies
4.  Start Monitora Server
5.  Run Monitora agent
6.  (Optional) Run junit tests in eclipse


TODO
--------
  *  Detailed description
  *  License clarification



---
Thanks to [GitHub Education](https://education.github.com) for support

Client TODO:
------------
*  Cifrado a nivel de https? encriptación de datos? los dos?
*  




XXX
=========

3 capas
Capa 1: app con hilos o lo que sea
Capa 2: negocio: cliente(ping, update, snapshot), scheduler, shell
Capa 3: BBDD, cliente
??? Modelo??? entre medias??? con lógica osin ella??? 

* Arrancar servicios: BD, Scheduler
* Hacer ping cada X tiempo
* Actualizar datos del agente
  * Con estos datos ejecutar scheduler
  * Cuando lo tenga todo, crear snapshot
  * Enviar snapshot
-- SERVICIOS DE MANTENIMIENTO
INSERT INTO servicio (nombre, tipo_servicio, estado_servicio) VALUES ('Cambio de Aceite y Filtro', 'MANTENIMIENTO', 'EN_ESPERA');
INSERT INTO paso (nombre, orden, completado, servicio_id) VALUES ('Drenaje del aceite usado.', 1, 0, 1);
INSERT INTO paso (nombre, orden, completado, servicio_id) VALUES ('Reemplazo del filtro de aceite.', 2, 0, 1);
INSERT INTO paso (nombre, orden, completado, servicio_id) VALUES ('Llenado con aceite nuevo.', 3, 0, 1);
INSERT INTO paso (nombre, orden, completado, servicio_id) VALUES ('Verificación de fugas.', 4, 0, 1);

INSERT INTO servicio (nombre, tipo_servicio, estado_servicio) VALUES ('Cambio de Filtros de Aire', 'MANTENIMIENTO', 'EN_ESPERA');
INSERT INTO paso (nombre, orden, completado, servicio_id) VALUES ('Localización y extracción del filtro de aire.', 1, 0, 2);
INSERT INTO paso (nombre, orden, completado, servicio_id) VALUES ('Inspección del filtro usado.', 2, 0, 2);
INSERT INTO paso (nombre, orden, completado, servicio_id) VALUES ('Instalación del nuevo filtro.', 3, 0, 2);
INSERT INTO paso (nombre, orden, completado, servicio_id) VALUES ('Prueba de funcionamiento.', 4, 0, 2);

INSERT INTO servicio (nombre, tipo_servicio, estado_servicio) VALUES ('Cambio de Filtros de Combustible', 'MANTENIMIENTO', 'EN_ESPERA');
INSERT INTO paso (nombre, orden, completado, servicio_id) VALUES ('Localización y extracción del filtro de combustible.', 1, 0, 3);
INSERT INTO paso (nombre, orden, completado, servicio_id) VALUES ('Inspección del filtro usado.', 2, 0, 3);
INSERT INTO paso (nombre, orden, completado, servicio_id) VALUES ('Instalación del nuevo filtro.', 3, 0, 3);
INSERT INTO paso (nombre, orden, completado, servicio_id) VALUES ('Prueba de funcionamiento.', 4, 0, 3);

INSERT INTO servicio (nombre, tipo_servicio, estado_servicio) VALUES ('Cambio de Bujías', 'MANTENIMIENTO', 'EN_ESPERA');
INSERT INTO paso (nombre, orden, completado, servicio_id) VALUES ('Extracción de las bujías usadas.', 1, 0, 4);
INSERT INTO paso (nombre, orden, completado, servicio_id) VALUES ('Inspección de las bujías.', 2, 0, 4);
INSERT INTO paso (nombre, orden, completado, servicio_id) VALUES ('Instalación de las nuevas bujías.', 3, 0, 4);
INSERT INTO paso (nombre, orden, completado, servicio_id) VALUES ('Prueba de funcionamiento.', 4, 0, 4);

INSERT INTO servicio (nombre, tipo_servicio, estado_servicio) VALUES ('Alineación y Balanceo de Ruedas', 'MANTENIMIENTO', 'EN_ESPERA');
INSERT INTO paso (nombre, orden, completado, servicio_id) VALUES ('Inspección visual de las ruedas.', 1, 0, 5);
INSERT INTO paso (nombre, orden, completado, servicio_id) VALUES ('Alineación de las ruedas.', 2, 0, 5);
INSERT INTO paso (nombre, orden, completado, servicio_id) VALUES ('Balanceo de las ruedas.', 3, 0, 5);
INSERT INTO paso (nombre, orden, completado, servicio_id) VALUES ('Prueba de manejo para verificar la alineación y el balanceo.', 4, 0, 5);

INSERT INTO servicio (nombre, tipo_servicio, estado_servicio) VALUES ('Revisión y Recarga del Sistema de Aire Acondicionado', 'MANTENIMIENTO', 'EN_ESPERA');
INSERT INTO paso (nombre, orden, completado, servicio_id) VALUES ('Inspección visual del sistema.', 1, 0, 6);
INSERT INTO paso (nombre, orden, completado, servicio_id) VALUES ('Recarga del refrigerante.', 2, 0, 6);
INSERT INTO paso (nombre, orden, completado, servicio_id) VALUES ('Comprobación de fugas.', 3, 0, 6);
INSERT INTO paso (nombre, orden, completado, servicio_id) VALUES ('Prueba de funcionamiento.', 4, 0, 6);

INSERT INTO servicio (nombre, tipo_servicio, estado_servicio) VALUES ('Revisión del Sistema de Frenos', 'MANTENIMIENTO', 'EN_ESPERA');
INSERT INTO paso (nombre, orden, completado, servicio_id) VALUES ('Inspección visual de las pastillas y discos de freno.', 1, 0, 7);
INSERT INTO paso (nombre, orden, completado, servicio_id) VALUES ('Comprobación del nivel de líquido de frenos.', 2, 0, 7);
INSERT INTO paso (nombre, orden, completado, servicio_id) VALUES ('Ajuste o reemplazo de pastillas o discos si es necesario.', 3, 0, 7);
INSERT INTO paso (nombre, orden, completado, servicio_id) VALUES ('Prueba de frenado.', 4, 0, 7);

INSERT INTO servicio (nombre, tipo_servicio, estado_servicio) VALUES ('Revisión y Mantenimiento de la Suspensión', 'MANTENIMIENTO', 'EN_ESPERA');
INSERT INTO paso (nombre, orden, completado, servicio_id) VALUES ('Inspección visual de los componentes de la suspensión.', 1, 0, 8);
INSERT INTO paso (nombre, orden, completado, servicio_id) VALUES ('Ajuste o reemplazo de amortiguadores o resortes si es necesario.', 2, 0, 8);
INSERT INTO paso (nombre, orden, completado, servicio_id) VALUES ('Lubricación de componentes.', 3, 0, 8);
INSERT INTO paso (nombre, orden, completado, servicio_id) VALUES ('Prueba de manejo para verificar la suspensión.', 4, 0, 8);

INSERT INTO servicio (nombre, tipo_servicio, estado_servicio) VALUES ('Revisión y Mantenimiento del Sistema de Dirección', 'MANTENIMIENTO', 'EN_ESPERA');
INSERT INTO paso (nombre, orden, completado, servicio_id) VALUES ('Inspección visual de la dirección.', 1, 0, 9);
INSERT INTO paso (nombre, orden, completado, servicio_id) VALUES ('Ajuste o reemplazo de piezas gastadas.', 2, 0, 9);
INSERT INTO paso (nombre, orden, completado, servicio_id) VALUES ('Alineación de la dirección si es necesario.', 3, 0, 9);
INSERT INTO paso (nombre, orden, completado, servicio_id) VALUES ('Prueba de manejo para verificar la dirección.', 4, 0, 9);

INSERT INTO servicio (nombre, tipo_servicio, estado_servicio) VALUES ('Revisión y Mantenimiento del Sistema de Escape', 'MANTENIMIENTO', 'EN_ESPERA');
INSERT INTO paso (nombre, orden, completado, servicio_id) VALUES ('Inspección visual del sistema de escape.', 1, 0, 10);
INSERT INTO paso (nombre, orden, completado, servicio_id) VALUES ('Reparación de fugas o daños.', 2, 0, 10);
INSERT INTO paso (nombre, orden, completado, servicio_id) VALUES ('Reemplazo de piezas gastadas.', 3, 0, 10);
INSERT INTO paso (nombre, orden, completado, servicio_id) VALUES ('Prueba de funcionamiento y de emisiones.', 4, 0, 10);

-- SERVICIOS DE REPARACION
INSERT INTO servicio (nombre, tipo_servicio, estado_servicio) VALUES ('Reparación de Sistema de Frenos', 'REPARACION', 'EN_ESPERA');
INSERT INTO paso (nombre, orden, completado, servicio_id) VALUES ('Identificación y reemplazo de componentes dañados.', 1, 0, 11);
INSERT INTO paso (nombre, orden, completado, servicio_id) VALUES ('Sangrado del sistema de frenos.', 2, 0, 11);
INSERT INTO paso (nombre, orden, completado, servicio_id) VALUES ('Ajuste y prueba de frenado.', 3, 0, 11);
INSERT INTO paso (nombre, orden, completado, servicio_id) VALUES ('Prueba de manejo.', 4, 0, 11);

INSERT INTO servicio (nombre, tipo_servicio, estado_servicio) VALUES ('Reparación de Sistema de Suspensión', 'REPARACION', 'EN_ESPERA');
INSERT INTO paso (nombre, orden, completado, servicio_id) VALUES ('Identificación y reemplazo de componentes dañados.', 1, 0, 12);
INSERT INTO paso (nombre, orden, completado, servicio_id) VALUES ('Alineación y balanceo.', 2, 0, 12);
INSERT INTO paso (nombre, orden, completado, servicio_id) VALUES ('Lubricación de componentes.', 3, 0, 12);
INSERT INTO paso (nombre, orden, completado, servicio_id) VALUES ('Prueba de manejo.', 4, 0, 12);

INSERT INTO servicio (nombre, tipo_servicio, estado_servicio) VALUES ('Reparación de Sistema de Dirección', 'REPARACION', 'EN_ESPERA');
INSERT INTO paso (nombre, orden, completado, servicio_id) VALUES ('Identificación y reemplazo de componentes dañados.', 1, 0, 13);
INSERT INTO paso (nombre, orden, completado, servicio_id) VALUES ('Alineación de la dirección.', 2, 0, 13);
INSERT INTO paso (nombre, orden, completado, servicio_id) VALUES ('Prueba de manejo para verificar la dirección.', 3, 0, 13);
INSERT INTO paso (nombre, orden, completado, servicio_id) VALUES ('Ajuste fino si es necesario.', 4, 0, 13);

INSERT INTO servicio (nombre, tipo_servicio, estado_servicio) VALUES ('Reparación de Sistema Eléctrico', 'REPARACION', 'EN_ESPERA');
INSERT INTO paso (nombre, orden, completado, servicio_id) VALUES ('Diagnóstico de fallas eléctricas.', 1, 0, 14);
INSERT INTO paso (nombre, orden, completado, servicio_id) VALUES ('Reemplazo de cables o conexiones dañadas.', 2, 0, 14);
INSERT INTO paso (nombre, orden, completado, servicio_id) VALUES ('Reparación de componentes electrónicos.', 3, 0, 14);
INSERT INTO paso (nombre, orden, completado, servicio_id) VALUES ('Prueba de funcionamiento.', 4, 0, 14);

INSERT INTO servicio (nombre, tipo_servicio, estado_servicio) VALUES ('Reparación de Sistema de Transmisión', 'REPARACION', 'EN_ESPERA');
INSERT INTO paso (nombre, orden, completado, servicio_id) VALUES ('Diagnóstico de fallas en la transmisión.', 1, 0, 15);
INSERT INTO paso (nombre, orden, completado, servicio_id) VALUES ('Reemplazo de piezas dañadas.', 2, 0, 15);
INSERT INTO paso (nombre, orden, completado, servicio_id) VALUES ('Ajuste de la transmisión.', 3, 0, 15);
INSERT INTO paso (nombre, orden, completado, servicio_id) VALUES ('Prueba de manejo para verificar la transmisión.', 4, 0, 15);

INSERT INTO servicio (nombre, tipo_servicio, estado_servicio) VALUES ('Reparación de Sistema de Escape', 'REPARACION', 'EN_ESPERA');
INSERT INTO paso (nombre, orden, completado, servicio_id) VALUES ('Identificación y reemplazo de componentes dañados.', 1, 0, 16);
INSERT INTO paso (nombre, orden, completado, servicio_id) VALUES ('Soldadura de tubos de escape.', 2, 0, 16);
INSERT INTO paso (nombre, orden, completado, servicio_id) VALUES ('Prueba de emisiones y funcionamiento.', 3, 0, 16);
INSERT INTO paso (nombre, orden, completado, servicio_id) VALUES ('Inspección visual final.', 4, 0, 16);

INSERT INTO servicio (nombre, tipo_servicio, estado_servicio) VALUES ('Reparación de Motor', 'REPARACION', 'EN_ESPERA');
INSERT INTO paso (nombre, orden, completado, servicio_id) VALUES ('Diagnóstico de problemas en el motor.', 1, 0, 17);
INSERT INTO paso (nombre, orden, completado, servicio_id) VALUES ('Reemplazo de piezas dañadas.', 2, 0, 17);
INSERT INTO paso (nombre, orden, completado, servicio_id) VALUES ('Ajuste de la sincronización y carburación.', 3, 0, 17);
INSERT INTO paso (nombre, orden, completado, servicio_id) VALUES ('Prueba de funcionamiento y rendimiento.', 4, 0, 17);

INSERT INTO servicio (nombre, tipo_servicio, estado_servicio) VALUES ('Reparación de Sistema de Refrigeración', 'REPARACION', 'EN_ESPERA');
INSERT INTO paso (nombre, orden, completado, servicio_id) VALUES ('Diagnóstico de problemas en el sistema de refrigeración.', 1, 0, 18);
INSERT INTO paso (nombre, orden, completado, servicio_id) VALUES ('Reemplazo de radiador o mangueras dañadas.', 2, 0, 18);
INSERT INTO paso (nombre, orden, completado, servicio_id) VALUES ('Llenado y purgado del sistema.', 3, 0, 18);
INSERT INTO paso (nombre, orden, completado, servicio_id) VALUES ('Prueba de temperatura y funcionamiento.', 4, 0, 18);

INSERT INTO servicio (nombre, tipo_servicio, estado_servicio) VALUES ('Reparación de Sistema de Climatización', 'REPARACION', 'EN_ESPERA');
INSERT INTO paso (nombre, orden, completado, servicio_id) VALUES ('Diagnóstico de problemas en el sistema de climatización.', 1, 0, 19);
INSERT INTO paso (nombre, orden, completado, servicio_id) VALUES ('Reemplazo de compresor, evaporador u otros componentes dañados.', 2, 0, 19);
INSERT INTO paso (nombre, orden, completado, servicio_id) VALUES ('Recarga de refrigerante.', 3, 0, 19);
INSERT INTO paso (nombre, orden, completado, servicio_id) VALUES ('Prueba de funcionamiento y temperatura.', 4, 0, 19);

INSERT INTO servicio (nombre, tipo_servicio, estado_servicio) VALUES ('Reparación de Carrocería y Pintura', 'REPARACION', 'EN_ESPERA');
INSERT INTO paso (nombre, orden, completado, servicio_id) VALUES ('Evaluación de daños en la carrocería.', 1, 0, 20);
INSERT INTO paso (nombre, orden, completado, servicio_id) VALUES ('Enderezado de chasis si es necesario.', 2, 0, 20);
INSERT INTO paso (nombre, orden, completado, servicio_id) VALUES ('Reparación de abolladuras y rasguños.', 3, 0, 20);
INSERT INTO paso (nombre, orden, completado, servicio_id) VALUES ('Pintura y pulido final.', 4, 0, 20);

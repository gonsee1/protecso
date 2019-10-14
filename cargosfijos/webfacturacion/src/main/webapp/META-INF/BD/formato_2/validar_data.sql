SELECT W.CLIENTE,count(*) FROM (SELECT a.CLIENTE,a.RUC FROM CLIENTE a GROUP BY a.CLIENTE,a.RUC) as W GROUP BY W.CLIENTE HAVING count(*)>1;-- cliente que se repiten diferentes ruc
SELECT W.RUC,count(*) FROM (SELECT a.CLIENTE,a.RUC FROM CLIENTE a GROUP BY a.CLIENTE,a.RUC) as W GROUP BY W.RUC HAVING count(*)>1;-- ruc que se repiten diferentes cliente

SELECT * FROM CLIENTE where RUC IN (20155670666,20504311342,20505946619);

-- ahora comparamos las dos tablas considerando que la tabla cliente es la correcta
-- verificamos que las razones sociales en la table servicios existen en la tabla cliente

SELECT * FROM (SELECT W.CLIENTE,(SELECT COUNT(*) FROM CLIENTE WHERE CLIENTE=W.CLIENTE) as son FROM (SELECT CLIENTE FROM CLIENTE_SERVICIO GROUP BY CLIENTE) AS W) AS Y WHERE Y.son=0;
INSERT INTO cliente (nombres, infoadic) VALUES ('Inversiones KP','Juliaca xd');
INSERT INTO cliente (nombres, infoadic) VALUES ('San Juan','Puno xd');

SELECT 
p.id, p.nombres
,sum(DISTINCT pm.adelanto_do) as total_adelanto_do
,sum(DISTINCT pm.adelanto_so) as total_adelanto_so
,sum(DISTINCT c.saldo_porpagar_do) + sum(DISTINCT pm.cobro_do) as total_porpagar_do
,sum(DISTINCT c.saldo_porpagar_so) + sum(DISTINCT pm.cobro_so) as total_porpagar_so

,sum(DISTINCT pm.adelanto_do) - sum(DISTINCT c.saldo_porpagar_do) - sum(DISTINCT pm.cobro_do) as saldo_do
,sum(DISTINCT pm.adelanto_so) - sum(DISTINCT c.saldo_porpagar_so) - sum(DISTINCT pm.cobro_so) as saldo_so

FROM compra as c  
	inner join proveedor as p on p.id = c.prove_id
	inner join prove_mov as pm on pm.prove_id = p.id
--WHERE p.id =3
GROUP BY p.id, p.nombres


SELECT 
	strftime('%Y-%m-%d', fecha)  as fecha 
	
	FROM caja_aper_cierre
	WHERE strftime('%Y-%m-%d', fecha) = "2020-06-01"
	
select fecha from compra 
where strftime('%d/%m/%Y', fecha) between "31/05/2020" and "31/05/2020";


SELECT 
p.id, p.nombres
, (SELECT coalesce( sum(DISTINCT adelanto_do)- sum(DISTINCT cobro_do), 0) FROM prove_mov WHERE prove_id  =p.id  )  as saldo_adelanto_do
, (SELECT coalesce( sum(DISTINCT adelanto_so)- sum(DISTINCT cobro_so), 0) FROM prove_mov WHERE prove_id  =p.id  )  as saldo_adelanto_so

, (SELECT coalesce( sum(DISTINCT saldo_porpagar_do), 0) FROM compra WHERE prove_id  =p.id  )  as total_porpagar_do
, (SELECT coalesce( sum(DISTINCT saldo_porpagar_so), 0) FROM compra WHERE prove_id  =p.id  )  as total_porpagar_so

, ((SELECT coalesce( sum(DISTINCT saldo_porpagar_do), 0) FROM compra WHERE prove_id  =p.id  ) -
   (SELECT coalesce( sum(DISTINCT adelanto_do)- sum(DISTINCT cobro_do), 0) FROM prove_mov WHERE prove_id  =p.id))  as saldo_do
   
, ((SELECT coalesce( sum(DISTINCT saldo_porpagar_so), 0) FROM compra WHERE prove_id  =p.id  ) -
   (SELECT coalesce( sum(DISTINCT adelanto_so)- sum(DISTINCT cobro_so), 0) FROM prove_mov WHERE prove_id  =p.id))  as saldo_so

FROM proveedor as p  
GROUP BY p.id, p.nombres


SELECT 
 sum(DISTINCT ingreso_cant_gr) as cant_gr
,sum(DISTINCT egreso_do) as egreso_do
,sum(DISTINCT egreso_so) as egreso_so
,sum(DISTINCT ingreso_do) as ingreso_do
,sum(DISTINCT ingreso_so) as ingreso_so
,sum(DISTINCT ingreso_do)-sum(DISTINCT egreso_do) as saldo_do
,sum(DISTINCT ingreso_so)-sum(DISTINCT egreso_so) as saldo_so
FROM
(
SELECT 
 sum(DISTINCT cant_gr) as ingreso_cant_gr
,sum(DISTINCT total_do) - sum(DISTINCT saldo_porpagar_do) as egreso_do
,sum(DISTINCT total_so) - sum(DISTINCT saldo_porpagar_so) as egreso_so
,0 as ingreso_do
,0 as ingreso_so
FROM compra
UNION
SELECT 
 0 as cant_gr
,sum(DISTINCT adelanto_do) as egreso_do
,sum(DISTINCT adelanto_so) as egreso_so
,sum(DISTINCT cobro_do) as ingreso_do
,sum(DISTINCT cobro_so) as ingreso_so
FROM prove_mov
) as G
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





SELECT 
*
FROM
(
 SELECT 
  id,
  fecha,
  prove_id,
  prove_nom,
  cant_gr as ingreso_cant_gr,
  onza,
  porc,
  ley,
  tcambio,
  precio_do,
  precio_so,
  total_do,
  total_so,
  saldo_porpagar_do,
  saldo_porpagar_so
 ,total_do - saldo_porpagar_do as egreso_do
 ,total_so - saldo_porpagar_so as egreso_so
 ,0 as ingreso_do
 ,0 as ingreso_so
 ,'' as glosa
 FROM compra
 UNION
 SELECT 
  id,
  fecha,
  prove_id,
  prove_nom,
  0 as ingreso_cant_gr,
  0 as onza,
  0 as porc,
  0 as ley,
  0 as tcambio,
  0 as precio_do,
  0 as precio_so,
  0 as total_do,
  0 as total_so,
  0 as saldo_porpagar_do,
  0 as saldo_porpagar_so
 ,adelanto_do as egreso_do
 ,adelanto_so as egreso_so
 ,cobro_do as ingreso_do
 ,cobro_so as ingreso_so
 ,glosa
 FROM prove_mov
) as G
ORDER BY fecha



SELECT 
*
FROM
(
 SELECT 
  id,
  fecha,
  prove_id,
  prove_nom,
  'Compra ' || cant_gr  || 'gr ('  ||  onza  || 'onza '  || porc  || '% '  || ley  || 'ley tc='  || tcambio  ||' '  ||
  CASE
	WHEN esdolares ==1   THEN 'pre$' || precio_do|| ') tot='  || total_do
	WHEN esdolares ==0   THEN 'preS/' || precio_so|| ') tot='  || total_so
	ELSE ')ERROR'
  END glosa
 ,0 as debito_do
 ,0 as debito_so
 ,saldo_porpagar_do as credito_do
 ,saldo_porpagar_so as credito_so
 FROM compra
 UNION
 SELECT 
  id,
  fecha,
  prove_id,
  prove_nom,
  glosa
 ,adelanto_do as debito_do
 ,adelanto_so as debito_so
 ,cobro_do as credito_do
 ,cobro_so as credito_so

 FROM prove_mov
) as G
ORDER BY fecha

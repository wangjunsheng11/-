package com.kakacl.product_service.mappers;



import java.util.List;
import java.util.Map;

public interface WorkCheckMapper  {

   Map findWorkCheckById(Map params);

   Map findWorkDayTime(Map params);

   Map findWorkNight(Map params);

   boolean updateWorkTime(Map params);

   boolean insertWorkStatus(Map params);

   List<Map> selectAccountId(Map params);
}

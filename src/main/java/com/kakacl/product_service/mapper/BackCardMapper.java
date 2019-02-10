package com.kakacl.product_service.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import java.util.*;

/*
 *
 * 用户收入mapper
 * @author wangwei
 * @date 2019/1/11
  * @param null
 * @return
 */
public interface BackCardMapper {

    /*
     * 查询银行规则
     *
     * @author wangwei
     * @date 2019/2/10
      * @param params
     * @return java.util.Map
     */
    @Select("select * from zzf_user_bank_rule where del_flag = 0")
    List<Map> selectBankRule(Map params);

    /*
     * 设置银行规则
     *
     * @author wangwei
     * @date 2019/2/10
     * @param params
     * @return java.util.Map
     */
    @Insert("INSERT INTO zzf_user_bank_rule (id, bank_name, bank_type, card_type, bank_sign, sign_path, `order`) VALUES (#{id},#{bank_name}, #{bank_type}, #{card_type}, #{bank_sign}, #{sign_path}, #{order})")
    boolean insertBankRule(Map params);

    /*
     *
     * 添加银行卡
     * @author wangwei
     * @date 2019/1/11
      * @param params
     * @return boolean
     */
    @Insert("INSERT INTO zzf_user_back_card (id, user_id, back_type, phone_num, card_num, id_card, create_time, create_by) VALUES (#{id}, #{user_id}, #{back_type}, #{phone_num}, #{card_num}, #{id_card}, #{create_time}, #{create_by})")
    boolean addCard(Map params);

    /*
     *
     * 根据用户主键查询银行卡集合
     * @author wangwei
     * @date 2019/1/11
      * @param params
     * @return java.util.List<java.util.Map>
     */
    @Select("SELECT * FROM zzf_user_back_card WHERE user_id = #{user_id} AND del_flag = 0")
    List<Map> selectList(Map params);

    /*
     *
     * 根据银行卡卡号查询银行卡
     * @author wangwei
     * @date 2019/1/11
     * @param params
     * @return java.util.List<java.util.Map>
     */
    @Select("SELECT * FROM zzf_user_back_card WHERE card_num = #{card_num} AND del_flag = 0")
    List<Map> selectBackCarcdExist(Map params);

    /*
     *
     * 根据主键逻辑删除数据
     * @author wangwei
     * @date 2019/1/11
      * @param params
     * @return boolean
     */
    @Update("UPDATE zzf_user_back_card SET del_flag=#{del_flag} WHERE (id = #{id})")
    boolean updateById(Map params);

    /*
     *
     * 根据用户主键逻辑删除银行卡
     * @author wangwei
     * @date 2019/1/11
      * @param params
     * @return boolean
     */
    @Update("UPDATE zzf_user_back_card SET del_flag=#{del_flag} WHERE (user_id = #{user_id} and card_num = #{card_num})")
    boolean updateByUserIdAndBackcardNum(Map params);

    /*
     *
     * 根据身份证号码查询用户信息
     * @author wangwei
     * @date 2019/1/11
      * @param params
     * @return java.util.List<java.util.Map>
     */
    @Select("SELECT * FROM zzf_user_info WHERE id_card = #{id_card}")
    List<Map> selectUSerByIdcard(Map params);

    /*
     *
     * 根据用户主键获取收入列表
     * @author wangwei
     * @date 2019/1/11
      * @param params
     * @return java.util.List<java.util.Map>
     */
    @Select("SELECT * FROM zzf_user_income WHERE user_id = #{user_id} AND del_flag = 0")
    List<Map> selectIncomeByUserid(Map params);

    /*
     *
     * 根据主键获取一个收入详情
     * @author wangwei
     * @date 2019/1/11
      * @param params
     * @return java.util.List<java.util.Map>
     */
    @Select("SELECT * FROM zzf_user_income_detail WHERE id = #{id}")
    Map selectIncomeDetail(Map params);
}

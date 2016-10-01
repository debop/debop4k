/*
 * Copyright (c) 2016. Sunghyouk Bae <sunghyouk.bae@gmail.com>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package debop4k.data.mybatis.typehandlers.mappers;

import debop4k.data.mybatis.typehandlers.models.UUIDBean;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

import java.util.UUID;

/**
 * @author sunghyouk.bae@gmail.com
 */
public interface UUIDBeanMapper {

  @Select("SELECT * FROM UUIDBeanTable where testId=#{id}")
  @ResultMap("debop4k.data.mybatis.typehandlers.selectUUIDResultMap")
  public UUIDBean findById(@Param("id") UUID id);

  @Select("DELETE FROM UUIDBeanTable where testId=#{id}")
  public Integer deleteById(@Param("id") UUID id);


  // Custom TypeHandler 에 대한 처리를 이렇게 해야만 하는가???
  @Insert("INSERT INTO UUIDBeanTable(testId, name, password) " +
          "VALUES(#{testId,jdbcType=OTHER,typeHandler=UUIDTypeHandler}, " +
          "       #{name}," +
//          "       #{password}" +
          "       #{password,jdbcType=VARCHAR,typeHandler=EncryptedStringTypeHandler}" +
          ")")
  public int save(UUIDBean uuidBean);
}

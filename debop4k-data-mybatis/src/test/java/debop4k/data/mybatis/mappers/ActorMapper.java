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

package debop4k.data.mybatis.mappers;

import debop4k.data.mybatis.models.Actor;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ActorMapper {

  @Select("SELECT * FROM Actors WHERE id = #{id}")
  Actor findById(@Param("id") int id);

  @Select("SELECT * FROM Actors WHERE firstname = #{firstname}")
  Actor findByFirstname(@Param("firstname") String firstname);

  @Select("SELECT * FROM Actors")
  List<Actor> findAll();

  @Delete("DELETE FROM Actors where id = #{id}")
  void deleteById(Integer id);

}

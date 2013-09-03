/* Copyright 2012 predic8 GmbH, www.predic8.com

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License. */

package com.predic8.wsdl

import groovy.xml.QName

import javax.xml.namespace.QName as JQName

import org.apache.commons.logging.*

import com.predic8.schema.*
import com.predic8.soamodel.*
import com.predic8.xml.util.*

class Types extends WSDLElement {

  private Log log = LogFactory.getLog(this.class)
  public static final JQName ELEMENTNAME = new JQName(Consts.WSDL11_NS, 'types')
  
  List<Schema> schemas = []
  
  protected parseChildren(token, child, WSDLParserContext ctx){
    super.parseChildren(token, child, ctx)
    switch (token.name ){
      case Schema.ELEMENTNAME :
      log.debug("new schema")
      log.debug("$definitions")
      def schema = new Schema(baseDir: definitions.baseDir, definitions: definitions, resourceResolver : definitions.resourceResolver)
      schema.parse(token, ctx)
			//Validating embeded schemas in WSDL
			 new SchemaValidator().validate(schema, ctx)
      schemas << schema ; break
    }
  }

  List<Schema> getAllSchemas() {
		(schemas + schemas.importedSchemas.flatten()).unique()
  }

  void create(AbstractCreator creator, CreatorContext ctx){
    creator.createTypes(this, ctx)
  }
	
}


/* Copyright 2008 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import javax.jcr.SimpleCredentials
import org.codehaus.groovy.grails.plugins.jcr.jackrabbit.RepositoryFactoryBean


/**
 * A plugin for the Grails framework (http://grails.org) that provides JackRabbit
 * as an implementation layer for the Grails JCR plugin.
 *
 * @author Sergey Nebolsin
 */
class JackrabbitGrailsPlugin {
    def version = '0.1-SNAPSHOT'

    def author = "Sergey Nebolsin"
    def authorEmail = "nebolsin@gmail.com"
    def title = "This plugin provides JackRabbit-based implementation for JCR plugin."
    def description = '''This plugin provides JackRabbit-based implementation for JCR plugin.'''
    def documentation = "http://grails.org/Jackrabbit+plugin"

    def dependsOn = [jcr: '0.1 > *']

    def doWithSpring = {
        jcrRepository(RepositoryFactoryBean) {
            configuration = "classpath:repository.xml"
            homeDir = "/repo"
        }

        jcrPassword(String, "")

        jcrCharArrayPassword(jcrPassword) {bean ->
            bean.factoryMethod = "toCharArray"
        }

        jcrCredentials(SimpleCredentials, "user", jcrCharArrayPassword)

        jcrSessionFactory(org.springmodules.jcr.JcrSessionFactory) {bean ->
            bean.singleton = true
            repository = jcrRepository
            credentials = jcrCredentials
        }

        jcrTemplate(org.springmodules.jcr.JcrTemplate) {
            sessionFactory = jcrSessionFactory
            allowCreate = true
        }

        jcrTransactionManager(org.springmodules.jcr.jackrabbit.LocalTransactionManager) {
            sessionFactory = jcrSessionFactory
        }
    }
}

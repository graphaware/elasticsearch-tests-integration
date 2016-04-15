Testing Support for Elasticsearch Integrations
==============================================

[![Build Status](https://travis-ci.org/graphaware/elasticsearch-tests-integration.png)](https://travis-ci.org/graphaware/elasticsearch-tests-integration) | Latest Release: 2.3.0.1

Provides the ability to test against Elasticsearch in the presence of potential dependency version clashes. A classic example
 are different versions of Lucene when running Neo4j and Elasticsearch in the same JVM.

This project starts Elasticsearch using a different classloader, hence alleviating the issue.

For Elasticsearch 2.3.0+, use

```
<dependency>
    <groupId>com.graphaware.integration.es</groupId>
    <artifactId>elasticsearch-tests-integration</artifactId>
    <version>2.3.0.1</version>
    <scope>test</scope>
</dependency>
```

For Elasticsearch 2.2.0+, use

```
<dependency>
    <groupId>com.graphaware.integration.es</groupId>
    <artifactId>elasticsearch-tests-integration</artifactId>
    <version>2.2.0.1</version>
    <scope>test</scope>
</dependency>
```

License
-------

Copyright (c) 2015-2016 GraphAware

GraphAware is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License
as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
You should have received a copy of the GNU General Public License along with this program.
If not, see <http://www.gnu.org/licenses/>.

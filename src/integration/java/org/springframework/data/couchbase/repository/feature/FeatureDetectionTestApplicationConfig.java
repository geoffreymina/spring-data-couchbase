package org.springframework.data.couchbase.repository.feature;

import java.util.Collections;
import java.util.List;

import com.couchbase.client.java.cluster.ClusterInfo;
import com.couchbase.client.java.cluster.DefaultClusterInfo;
import com.couchbase.client.java.document.json.JsonObject;
import com.couchbase.client.java.env.CouchbaseEnvironment;
import com.couchbase.client.java.env.DefaultCouchbaseEnvironment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.couchbase.config.AbstractCouchbaseConfiguration;
import org.springframework.data.couchbase.core.CouchbaseTemplate;
import org.springframework.data.couchbase.core.WriteResultChecking;
import org.springframework.data.couchbase.repository.support.IndexManager;

@Configuration
public class FeatureDetectionTestApplicationConfig extends AbstractCouchbaseConfiguration {

  @Autowired
  private Environment springEnv;

  @Bean
  public String couchbaseAdminUser() {
    return springEnv.getProperty("couchbase.adminUser", "Administrator");
  }

  @Bean
  public String couchbaseAdminPassword() {
    return springEnv.getProperty("couchbase.adminUser", "password");
  }

  @Override
  protected List<String> getBootstrapHosts() {
    return Collections.singletonList(springEnv.getProperty("couchbase.host", "127.0.0.1"));
  }

  @Override
  protected String getBucketName() {
    return springEnv.getProperty("couchbase.bucket", "default");
  }

  @Override
  protected String getBucketPassword() {
    return springEnv.getProperty("couchbase.password", "");
  }


  @Override
  protected CouchbaseEnvironment getEnvironment() {
    return DefaultCouchbaseEnvironment.builder()
        .connectTimeout(10000)
        .kvTimeout(10000)
        .queryTimeout(10000)
        .viewTimeout(10000)
        .build();
  }

  @Override
  public CouchbaseTemplate couchbaseTemplate() throws Exception {
    CouchbaseTemplate template = super.couchbaseTemplate();
    template.setWriteResultChecking(WriteResultChecking.LOG);
    return template;
  }

  @Override
  public ClusterInfo couchbaseClusterInfo() throws Exception {
    return new DefaultClusterInfo(JsonObject.empty());
  }

  //this is for dev so it is ok to auto-create indexes
  @Override
  public IndexManager indexManager() {
    return new IndexManager();
  }

  //change the name of the field that will hold type information
  @Override
  public String typeKey() {
    return "javaClass";
  }
}

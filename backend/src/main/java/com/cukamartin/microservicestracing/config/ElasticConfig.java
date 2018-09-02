package com.cukamartin.microservicestracing.config;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import java.net.InetAddress;

/**
 * Elastic 2.x fungoval na TransportClient client = new TransportClient()....
 * Elastic 5.0 - 5.5.0 funguje na TransportClient = new PreBuiltTransportClient() - bolo zmenene API, podporu spring data elastic ma az od Spring-Boot2.x
 * Elastic 5.6.8 je odporucane konfigurovat cez HighRestClient nakolko TransportClient bude deprecated
 * Elastic 6.x este nema podporu Spring-Data-ElasticSearch bude mat az ked vyjde spring data elasic 3.1.x (30.5.2017 je len 3.0.7)
 *
 * Ked sa spusta elastic pre testovacie ucely cez docker tak treba nastavit http.host a transport.host na same 0,
 * vypnut xpack.security a vypnut client.transport.sniff. Docker obaluje elastic e zle funguje na localhoste preto potrebne zmeny...
 * https://stackoverflow.com/questions/44709297/how-can-i-connect-to-elasticsearch-running-in-the-official-docker-image-using-tr
 *
 * Po spusteni elasticu sa mozem pozriet na status cez http://localhost:9200/_cat/health (status yellow znemana ze mam len 1 node, to je vporiadku na testy)
 *
 * docker network create mynetwork --driver=bridge
 * docker run -p 9200:9200 -p 9300:9300 -e "http.host=0.0.0.0" -e "transport.host=0.0.0.0" -e "xpack.security.enabled=false" -d --name microelastic -d --network mynetwork docker.elastic.co/elasticsearch/elasticsearch:5.5.0
 * docker run -d --network mynetwork -e ELASTICSEARCH_URL=http://microelastic:9200 --name microkibana -p 5601:5601 kibana:5.5.0
 * potom uz staci spustat len
 * docker start microelastic
 * docker start microkibana
 *
 * cez postmana
 * [POST] http://localhost:8080/api/trace
 * {
 * "id": "1",
 * "title": "Never Split the Difference",
 * "author": "Chris Voss & Tahi Raz",
 * "releaseDate": "07.04.2018"
 * }
 *
 * vytvorit v kibane http://localhost:5601/app/kibana#/discover?_g=() novy index mkyong
 * budeme vidiet vlozeny trace
 */
@Configuration
@EnableElasticsearchRepositories(basePackages = "com.cukamartin.microservicestracing.repository")
public class ElasticConfig {

    @Value("${elasticsearch.host}")
    private String esHost;

    @Value("${elasticsearch.port}")
    private int esPort;

    @Bean
    public Client client() throws Exception {

        Settings esSettings = Settings.builder()
                .put("cluster.name", "docker-cluster")
                .put("client.transport.sniff", false)
                .build();


        TransportClient client = new PreBuiltTransportClient(esSettings)
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(esHost), esPort));
        return client;
    }

    @Bean
    public ElasticsearchOperations elasticsearchTemplate() throws Exception {
        return new ElasticsearchTemplate(client());
    }
}

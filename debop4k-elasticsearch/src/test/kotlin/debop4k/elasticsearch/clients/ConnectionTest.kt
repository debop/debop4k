package debop4k.elasticsearch.clients

import debop4k.elasticsearch.AbstractElasticSearchTest
import org.elasticsearch.client.transport.TransportClient
import org.elasticsearch.common.transport.InetSocketTransportAddress
import org.junit.Test
import java.net.InetSocketAddress


class ConnectionTest : AbstractElasticSearchTest() {

  @Test fun connectServer() {
    val client = TransportClient
        .builder()
        .build()
        .addTransportAddress(InetSocketTransportAddress(InetSocketAddress("localhost", 9300)))

    println("### nodes")

    client.listedNodes().forEach {
      println("node: $it")
    }

    client.close()
  }
}
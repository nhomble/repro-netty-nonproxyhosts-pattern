package io.github.nhomble.netty.nonproxyhostissue;

import org.junit.jupiter.api.Test;
import reactor.netty.http.client.HttpClient;
import reactor.netty.tcp.ProxyProvider;

import static org.junit.jupiter.api.Assertions.*;

public class NonProxyHostsTest {

    private final String NON_PROXY_HOSTS = "*.some.domain";

    @Test
    void nettyClientFails() {
        HttpClient client = HttpClient
                .create()
                .tcpConfiguration(c -> c.proxy(po -> po.type(ProxyProvider.Proxy.HTTP).host("forward.proxy").nonProxyHosts(NON_PROXY_HOSTS)));
        RuntimeException e = assertThrows(RuntimeException.class, () -> client.get().uri("127.0.0.1").response().block());
        String msg = e.getMessage();
        assertTrue(msg.contains("Dangling meta character '*' near index 0"));
        assertTrue(msg.contains("*.some.domain"));
    }
}

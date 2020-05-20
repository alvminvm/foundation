package me.alzz.okhttp


import okhttp3.internal.tls.OkHostnameVerifier
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLSession

/**
 * Created by jerry on 2018/8/1.
 */

object SnifferVerifier: HostnameVerifier {

    private val snifferHostnames = arrayOf(
        "fiddler.com",
        "fiddler2.com",
        "charlesproxy.com",
        "wproxy.org",
        "Packet Capture CA Certificate",
        "mitmproxy",
        "Debug Proxy",
        "HttpCanary"
    )

    override fun verify(hostname: String, session: SSLSession): Boolean {
        for (cert in session.peerCertificateChain) {

            val name = cert.issuerDN?.name ?: continue
            if (name.isEmpty()) { continue }

            for (sniffer in snifferHostnames) {

                //证书信息有抓包工具的证书信息
                if (name.contains(sniffer, true)) {
                    return false
                }
            }

        }

        return OkHostnameVerifier.verify(hostname, session)
    }
}

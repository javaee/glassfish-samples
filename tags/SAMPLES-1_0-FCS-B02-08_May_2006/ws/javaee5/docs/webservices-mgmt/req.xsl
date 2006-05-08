<?xml version="1.0" ?> 
  <xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:env="http://schemas.xmlsoap.org/soap/envelope/" xmlns:ns0="http://hello.org/wsdl/HelloWorld" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"> 
    <xsl:output method="xml" encoding="utf-8" undeclare-prefixes="no"/> 
    <xsl:template match="/">
    <xsl:text>
    <env:Envelope xmlns:env="http://schemas.xmlsoap.org/soap/envelope/" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:enc="http://schemas.xmlsoap.org/soap/encoding/" xmlns:ns0="http://hello.org/wsdl/HelloWorld" env:encodingStyle="http://schemas.xmlsoap.org/soap/encoding/">
<env:Body>
<ns0:sayHello>
<String_1 xsi:type="xsd:string">Satish!</String_1>
</ns0:sayHello>
</env:Body>
</env:Envelope>
    </xsl:text>
    </xsl:template>
    </xsl:stylesheet>

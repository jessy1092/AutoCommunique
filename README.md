AutoCommunique
==============

#公報自動化

##主旨
簡少公報編輯/整理時人力的浪費。

##功能
可以設置 padId 讀取 hackpad 上的公報，依據公報上的 Tag 作分類，產生彙整好的公報，並自動轉發到 hackpad 跟 tumblr 上。

##使用方式

* 增加 api_key.txt
	* 第一行為讀取與寫入公報 hackpad，需要 Client ID 與 Secret，在 setting 可以找到。
	* 第二行為使用tumblr所必需，為 Cunsumer Key 與 Cunsumer Secret。
	* 第三行為使用tumblr所必需，為 Token Key 與 Token Secret。
* 增加 communiques.txt，為每週公報的 padid 。
* 增加 title.txt 設置每月公報的標題。

##使用的jar

* [jdk 1.7](http://www.oracle.com/technetwork/java/javase/downloads/index.html)
* [jersey-1.17.1](http://jersey.java.net/index.html)
* [oauth-client-1.17.1](http://mvnrepository.com/artifact/com.sun.jersey.contribs.jersey-oauth/oauth-client)
* [oauth-signature-1.17.1](http://mvnrepository.com/artifact/com.sun.jersey.contribs.jersey-oauth/oauth-signature)
* [jackson-all-1.9.11](http://wiki.fasterxml.com/JacksonDownload)
* [jumblr-0.0.6](https://github.com/tumblr/jumblr)
* [gson-2.2.4](https://code.google.com/p/google-gson/downloads/detail?name=google-gson-2.2.4-release.zip&can=2&q=)
* [scribe-1.3.5](http://mvnrepository.com/artifact/org.scribe/scribe/1.3.5)


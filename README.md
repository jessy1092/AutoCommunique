AutoCommunique
==============

#主旨
簡少公報編輯/整理時人力的浪費。

#功能
可以設置 padId 讀取 hackpad 上的公報，依據公報上的 Tag 作分類，產生彙整好的公報，並自動轉發到 hackpad 跟 tumblr 上。

#使用方式

* 增加 api_keys.txt
	* 第一行為讀取與寫入公報 hackpad，需要 Client ID 與 Secret，在 setting 可以找到。
	* 第二行為使用tumblr所必需，為 Cunsumer Key 與 Cunsumer Secret。
	* 第三行為使用tumblr所必需，為 Token Key 與 Token Secret。
* 增加 communiques.txt，為每週公報的 padid 。
* 增加 title.txt 設置每月公報的標題。

#Output 

* 產生 pads.txt 裡面為月份公報的 Pad ID

#Maven

* Using Hackpad Java API - [Jackpad](https://github.com/jessy1092/jackpad)

#License

The code is available at github project under [MIT License](https://github.com/jessy1092/AutoCommunique/blob/master/LICENSE)

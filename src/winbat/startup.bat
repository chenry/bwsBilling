echo off

start javaw.exe -Djava.library.path=./cfg -cp ./cfg;./lib/bwsBilling.jar;./lib/swt.jar;./lib/log4j-1.2.8.jar;./lib/junit.jar;./lib/mysql-connector-java-5.0.4-bin.jar;./cfg/swt-win32-3062.dll;./lib/commons-beanutils.jar;./lib/commons-collections.jar;./lib/commons-digester-1.7.jar;./lib/commons-logging.jar;./lib/commons-logging-api.jar;./lib/jasperreports-1.3.0.jar;./lib/xercesImpl.jar;./lib/xmlParserAPIs.jar;./lib/itext-1.02b.jar;./lib/poi-2.0-final-20040126.jar com.hs.bws.ui.BWSMainUI

exi
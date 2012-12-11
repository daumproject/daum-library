

g++ -I"/usr/lib/jvm/java-6-sun-1.6.0.30/include"  -I"/usr/lib/jvm/java-6-sun-1.6.0.30/include/linux" -o com_couchbase_touchdb_TDCollateJSON.so -shared -Wl,-soname,com_couchbase_touchdb_TDCollateJSON.cpp -static  -ldl

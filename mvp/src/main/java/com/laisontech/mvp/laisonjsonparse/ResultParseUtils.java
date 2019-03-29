package com.laisontech.mvp.laisonjsonparse;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.laisontech.mvp.utils.GzipUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by SDP on 2017/6/14.
 * 对json结果解析帮助类
 */
public class ResultParseUtils {
    public static final int ERROR_CODE_SUCCESS = 0;//解析结果正确
    public static final int FORMAT_ERROR = -1;//json数据格式错误
    private static final int NO_COMPRESS = 0;
    private static final int COMPRESS = 1;

    /**
     * @param json     json数据
     * @param clzInner 要得到的data
     * @param type     要得类型
     */
    public static ParseResultBean getResult(String json, Class<?> clzInner, ResultType type) {
        ParseResultBean resultBean;
        if (json == null || TextUtils.isEmpty(json) || !checkStrIsJsonFormat(json)) {
            resultBean = new ParseResultBean(FORMAT_ERROR, null);
            return resultBean;
        }
        JsonModel httpJsonMoudle;
        try {
            httpJsonMoudle = new Gson().fromJson(json, JsonModel.class);
        } catch (Exception e) {
            resultBean = new ParseResultBean(FORMAT_ERROR, null);
            return resultBean;
        }
        if (httpJsonMoudle == null) {
            resultBean = new ParseResultBean(FORMAT_ERROR, null);
            return resultBean;
        }
        //获取errorCode
        int errorCode = httpJsonMoudle.getErrorcode();
        //根据压缩标志进行解析数据
        int gzipCompress = httpJsonMoudle.getGzipcompress();
        //获取Object对象
        Object data = httpJsonMoudle.getData();
        resultBean = new ParseResultBean();
        resultBean.setErrorCode(errorCode);
        if (errorCode == ERROR_CODE_SUCCESS && data != null) {
            //未压缩
            if (NO_COMPRESS == gzipCompress) {
                String dataJson = new Gson().toJson(data);
                //获取的集合类
                resultBean = getParseResultBean(json, clzInner, type, resultBean, data, dataJson);
            } else if (COMPRESS == gzipCompress) {  //压缩执行步骤跟上述查不多
                //转为字符串
                String compressData = data.toString();
                //执行解压缩,如果是以引号开头和结尾的话，则去除开头可结尾的引号
                if ((compressData.startsWith("\"") && compressData.endsWith("\""))) {
                    compressData = compressData.substring(1, compressData.length() - 1);
                }
                String dataJson = GzipUtils.decompress_GZIP(compressData);//对压缩的信息执行解压缩操作
                resultBean = getParseResultBean(json, clzInner, type, resultBean, data, dataJson);
            }
        }
        return resultBean;
    }

    private static ParseResultBean getParseResultBean(String json, Class<?> clzInner, ResultType type, ParseResultBean resultBean, Object data, String dataJson) {
        if (!dataJson.isEmpty()) {
            try {
                if (dataJson.startsWith("[")) {
                    if (type == ResultType.LIST) {
                        resultBean.setParseResult(jsonToArrayList(dataJson, clzInner));
                    }
                    //获取的对象类
                } else if (dataJson.startsWith("{")) {
                    if (type == ResultType.OBJECT) {
                        resultBean.setParseResult(new Gson().fromJson(dataJson, clzInner));
                    }
                } else if (dataJson.startsWith("\"{") && dataJson.endsWith("}\"")) {
                    if (type == ResultType.OBJECT) {
                        dataJson = dataJson.substring(1, dataJson.length() - 1);
                        dataJson = dataJson.replaceAll("\\\\", "");
                        resultBean.setParseResult(new Gson().fromJson(dataJson, clzInner));
                    }
                } else if (dataJson.startsWith("\"[")) {
                    if (type == ResultType.LIST) {
                        resultBean.setParseResult(getArrayList(data, clzInner));
                    }
                } else {
                    if (type == ResultType.STRING) {
                        resultBean.setParseResult(jsonStr(json));
                    }
                }
            } catch (Exception e) {
                resultBean = new ParseResultBean(FORMAT_ERROR, null);
            }
        } else {
            resultBean = new ParseResultBean(FORMAT_ERROR, null);
        }
        return resultBean;
    }

    //普通json类

    private static String jsonStr(String json) {
        if (json.length() >= 3) {
            return json.substring(1, json.length() - 1);
        }
        return "";
    }

    //查看是不是json数据
    public static boolean checkStrIsJsonFormat(String json) {
        try {
            new JsonParser().parse(json);
            return true;
        } catch (JsonParseException e) {
            return false;
        }
    }

    //将纯数组转换为list
    private static <T> ArrayList<T> jsonToArrayList(String json, Class<T> clazz) {
        Log.e("DATA", json);
        Type type = new TypeToken<ArrayList<JsonObject>>() {
        }.getType();
        ArrayList<JsonObject> jsonObjects = new Gson().fromJson(json, type);
        ArrayList<T> arrayList = new ArrayList<>();
        for (JsonObject jsonObject : jsonObjects) {
            arrayList.add(new Gson().fromJson(jsonObject, clazz));
        }
        return arrayList;
    }

    //简单数组转化
    public static ArrayList<?> getArrayList(Object obj, Class<?> clz) {
        if (obj == null) {
            return null;
        }
        String json = new Gson().toJson(obj);
        boolean b = checkStrIsJsonFormat(json);
        if (!b) {
            return null;
        }
        return jsonToArrayList(json, clz);
    }

    public static ArrayList<?> getArray(Object object, Class<?> clz) {
        String json = new Gson().toJson(object);
        if (json.startsWith("\"") && json.length() >= 3) {
            json = json.substring(1, json.length() - 1);
        }
        //Json的解析类对象
        JsonParser parser = new JsonParser();
        //将JSON的String 转成一个JsonArray对象
        JsonArray jsonArray = parser.parse(json).getAsJsonArray();
        Gson gson = new Gson();
        ArrayList<Object> arrayList = new ArrayList<>();
        //加强for循环遍历JsonArray
        for (JsonElement user : jsonArray) {
            //使用GSON，直接转成Bean对象
            Object obj = gson.fromJson(user, clz);
            arrayList.add(obj);
        }
        return arrayList;
    }

    /**
     * 解析的类型
     */
    public enum ResultType {
        OBJECT,//对象类型
        LIST,//List 类型
        STRING,//String类型
    }

    /**
     * 解析的类
     */
    public static class ParseResultBean {
        int errorCode;
        Object parseResult;//类型 String,bean,List<Bean>


        public ParseResultBean() {
        }

        public ParseResultBean(int errorCode, Object parseResult) {
            this.errorCode = errorCode;
            this.parseResult = parseResult;
        }

        public int getErrorCode() {
            return errorCode;
        }

        public void setErrorCode(int errorCode) {
            this.errorCode = errorCode;
        }

        public Object getParseResult() {
            return parseResult;
        }

        public void setParseResult(Object parseResult) {
            this.parseResult = parseResult;
        }


        @Override
        public String toString() {
            return "ParseResultBean{" +
                    "errorCode=" + errorCode +
                    ", parseResult=" + parseResult +
                    '}';
        }
    }

}

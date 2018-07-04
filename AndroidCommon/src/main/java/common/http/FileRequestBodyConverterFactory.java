package common.http;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Converter;
import retrofit2.Retrofit;
/**g
 * Created by ZZH on 2018/1/29
 *
 * @date: 2018/1/29 下午4:38
 * @email: zzh_hz@126.com
 * @QQ: 1299234582
 * @author: zzh
 * @Description: 
 */
public  class FileRequestBodyConverterFactory extends Converter.Factory {
    @Override
    public Converter<File, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        return new FileRequestBodyConverter();
    }
}

class FileRequestBodyConverter implements Converter<File, RequestBody> {

    @Override
    public RequestBody convert(File file) throws IOException {
        return RequestBody.create(MediaType.parse("application/otcet-stream"), file);
    }
}
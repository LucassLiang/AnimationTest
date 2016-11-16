package data.service;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;
import data.dto.ImageDTO;

/**
 * Created by lucas on 11/4/16.
 */

public interface ImageService {
    @GET("data/imgs?col=壁纸&pn=1&sort=0&rn=25&p=channel&from=1")
    Observable<ImageDTO> getPicture(@Query("tag") String tag);
}

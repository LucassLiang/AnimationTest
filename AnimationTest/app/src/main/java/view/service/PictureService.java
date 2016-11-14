package view.service;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;
import view.dto.ImageDTO;

/**
 * Created by lucas on 11/4/16.
 */

public interface PictureService {
    @GET("data/imgs?&sort=0&rn=25&p=channel&from=1")
    Observable<ImageDTO> getPicture(@Query("col") String col, @Query("tag") String tag, @Query("pn") String pn);
}

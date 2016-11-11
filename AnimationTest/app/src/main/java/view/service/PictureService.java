package view.service;

import retrofit2.http.GET;
import rx.Observable;
import view.dto.ImageDTO;

/**
 * Created by lucas on 11/4/16.
 */

public interface PictureService {
    @GET("data/imgs?col=摄影&tag=风景&sort=0&pn=2&rn=25&p=channel&from=1")
    Observable<ImageDTO> getPicture();
}

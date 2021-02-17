package TJ_8_MediaCtrl;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;

import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;

public class RootController implements Initializable {
    @FXML private MediaView mediaView;
    @FXML private Button btnPlay;
    @FXML private Button btnPause;
    @FXML private Button btnStop;
    @FXML private Button btnChangeMedia;

    @FXML private ImageView imageViewVolume;
    @FXML private ProgressBar progressBar;
    @FXML private Slider sliderVolume;
    @FXML private Slider sliderTime;
    @FXML private Label labelTime;

    private boolean endOfMedia;

    // 현재 플레이중인 미디어 리스트.
    // 지금은 두개여서 video, audio로 했는데, 목록이 추가되면 쉽게 바꿀 수 있다.
    private PlayList NowOnPlaying;
    enum PlayList{
        VIDEO,
        AUDIO
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // 미디어 변경 버튼의 이벤트를 설정해준다.
        btnChangeMedia.setOnAction(event->handleBtnChangeAction(event,mediaView));

        // 초기 미디어플레이어는 VIDEO로 해준다.
        this.NowOnPlaying = PlayList.VIDEO;
        MediaPlayer mediaPlayer = changeMedia(NowOnPlaying);
        mediaView.setMediaPlayer(mediaPlayer);

        // 초기 볼륭값을 20으로 준다.
        sliderVolume.setValue(20.0);
    }

    public void handleBtnPlayAction(ActionEvent event, MediaPlayer mediaPlayer){
        if(endOfMedia){
            mediaPlayer.stop();
            mediaPlayer.seek(mediaPlayer.getStartTime());
        }
        mediaPlayer.play();
        endOfMedia = false;
    }
    public void handleBtnPauseAction(ActionEvent event, MediaPlayer mediaPlayer){
        mediaPlayer.pause();
    }
    public void handleBtnStopAction(ActionEvent event, MediaPlayer mediaPlayer){
        mediaPlayer.stop();
    }
    // 미디어를 바꿔주는 버튼이벤트
    public void handleBtnChangeAction(ActionEvent event, MediaView mediaView){
        if(this.NowOnPlaying == PlayList.AUDIO) {
            // 오디오든 비디오든 다 false다. 음. .
            System.out.println("현재 미디어 : 오디오, mediaView.getMediaPlayer().isAutoPlay() : "
                                + mediaView.getMediaPlayer().isAutoPlay());

            // 현재 미디어가 오디오라면 -> 비디오로 바꾼다.
            btnChangeMedia.setText("music");
            mediaView.setMediaPlayer(changeMedia(PlayList.VIDEO));
        }
        else{

            System.out.println("현재 미디어 : 비디오, mediaView.getMediaPlayer().isAutoPlay() : "
                    + mediaView.getMediaPlayer().isAutoPlay());

            btnChangeMedia.setText("video");
            mediaView.setMediaPlayer(changeMedia(PlayList.AUDIO));
        }
    }

    // NowOnPlaying에 따라 새로운 미디어를 설정해주는 메서드
    private MediaPlayer changeMedia(PlayList playList){
        // 미디어를 중간에 바꾸는 경우, 일단 미디어를 중지시킨다.
        if(mediaView.getMediaPlayer() != null) {
            mediaView.getMediaPlayer().stop();
        }
        switch (playList){
            case VIDEO -> {
                this.NowOnPlaying = PlayList.VIDEO;
                return getVideoPlayer();
            }
            case AUDIO -> {
                this.NowOnPlaying = PlayList.AUDIO;
                return getAudioPlayer();
            }
            default ->{
                System.out.println("Wrong PlayList");
                return null;
            }
        }
    }

    // 실제 미디어를 만들고, 설정한 뒤 리턴하는 팩토리메서드 둘
    private MediaPlayer getVideoPlayer(){
        Media media = new Media(getClass().
                getResource("Medias/How To Draw Perfect Circle.mp4").toString());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        setMyPlayer(mediaPlayer);
        return mediaPlayer;
    }
    private MediaPlayer getAudioPlayer(){
        Media media = new Media(getClass().
                getResource("Medias/Righteous_Path.mp3").toString());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        setMyPlayer(mediaPlayer);
        return mediaPlayer;
    }

    // 새로 만든 플레이어를 세팅하는 메서드
    private void setMyPlayer(MediaPlayer mediaPlayer ){
        mediaPlayer.setOnReady(()->{
            btnPlay.setDisable(false); // 레디 상태에서는, Play버튼만 누를수있다.
            btnPause.setDisable(true);
            btnStop.setDisable(true);
            // 플레이어가 READY 되면 볼륨도 세팅해준다.
            mediaPlayer.setVolume(sliderVolume.getValue() / 100);

            // 플레이어의 재생시간이 변경될때마다 프로그래스 정도도 처리해준다.
            mediaPlayer.currentTimeProperty().addListener((observableValue, duration, t1) -> {
                double progress =  mediaPlayer.getCurrentTime().toSeconds()
                        / mediaPlayer.getTotalDuration().toSeconds();
                progressBar.setProgress(progress);

                labelTime.setText(
                        String.format("%.1f / %.1f",
                                mediaPlayer.getCurrentTime().toSeconds(),
                                mediaPlayer.getTotalDuration().toSeconds())
                );

                sliderTime.setValue(progress * 100);

            });

            // 볼륨 슬라이더의 속성감시를 해준다.
            sliderVolume.valueProperty().addListener((observableValue, oldValue, newValue) -> {
                // 플레이어의 볼륨은 [0,1]  슬라이더의 value는 [0,100] 이므로 조정이 필요
                mediaPlayer.setVolume(newValue.doubleValue() / 100 );

                if(sliderVolume.getValue() == 0.0){
                    imageViewVolume.setImage(new Image(
                        getClass().getResource("Medias/VolumeImages/VolumeX.jpg").toString()));
                }
                else{
                    imageViewVolume.setImage(new Image(
                        getClass().getResource("Medias/VolumeImages/VolumeO.jpg").toString()));
                }
            });

            sliderTime.valueProperty().addListener((observableValue, oldValue, newValue) -> {
                if (sliderTime.isPressed()) {
                    double TimeRate = newValue.doubleValue() / 100.0;
                    double settingTime = TimeRate * mediaPlayer.getTotalDuration().toSeconds();
                    Duration duration = Duration.seconds(settingTime);
                    mediaPlayer.seek(duration);
                }
            });

        });
        mediaPlayer.setOnPlaying(()->{
            btnPlay.setDisable(true); // 플레잉 상태에서는 Play버튼을 누를수없다.
            btnPause.setDisable(false);
            btnStop.setDisable(false);
        });
        mediaPlayer.setOnPaused(()->{
            btnPlay.setDisable(false);
            btnPause.setDisable(true);
            btnStop.setDisable(false);
        });
        mediaPlayer.setOnStopped(()->{
            btnPlay.setDisable(false);
            btnPause.setDisable(true);
            btnStop.setDisable(true);
        });
        mediaPlayer.setOnEndOfMedia(()->{
            endOfMedia = true;
            btnPlay.setDisable(false);
            btnPause.setDisable(true);
            btnStop.setDisable(true);

            progressBar.setProgress(1.0);
            labelTime.setText(
                    String.format("%.1f / %.1f",
                            mediaPlayer.getTotalDuration().toSeconds(),
                            mediaPlayer.getTotalDuration().toSeconds())
            );
        });

        btnPlay.setOnAction(event->handleBtnPlayAction(event,mediaPlayer));
        btnPause.setOnAction(event->handleBtnPauseAction(event,mediaPlayer));
        btnStop.setOnAction(event->handleBtnStopAction(event,mediaPlayer));
    }

}

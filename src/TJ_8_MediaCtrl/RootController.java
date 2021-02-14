package TJ_8_MediaCtrl;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

import java.net.URL;
import java.util.ResourceBundle;

public class RootController implements Initializable {
    @FXML private MediaView mediaView;
    @FXML private Button btnPlay;
    @FXML private Button btnPause;
    @FXML private Button btnStop;
    @FXML private Button btnChangeMedia;

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
        });

        btnPlay.setOnAction(event->handleBtnPlayAction(event,mediaPlayer));
        btnPause.setOnAction(event->handleBtnPauseAction(event,mediaPlayer));
        btnStop.setOnAction(event->handleBtnStopAction(event,mediaPlayer));
    }

}

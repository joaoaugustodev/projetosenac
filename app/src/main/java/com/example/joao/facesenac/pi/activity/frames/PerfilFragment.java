package com.example.joao.facesenac.pi.activity.frames;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.joao.facesenac.R;
import com.example.joao.facesenac.pi.activity.activity.FeedActivity;
import com.example.joao.facesenac.pi.activity.interfaces.ApiUsers;
import com.example.joao.facesenac.pi.activity.model.Curtidas;
import com.example.joao.facesenac.pi.activity.model.GetFeed;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PerfilFragment extends Fragment {
    private EditText texto;
    private ProgressBar pprogressBarPost;
    private ProgressBar progressBarLoading;
    private Long idPosts;
    private FeedActivity activitie;
    private ViewGroup mensagens;
    private TextView idUser;
    private TextView idHistorico;
    private TextView nomeFeed;
    private TextView dataFeed;
    private TextView descFeed;
    private ImageView imageFeedDesc;
    private ImageView imagemFeed;
    private Button comentar;
    private String textnome, textEmail;
    private Boolean foto;


    public PerfilFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);
        activitie = (FeedActivity) getActivity();
        idPosts = activitie.returnId();
        mensagens = view.findViewById(R.id.containerperfil);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            textnome = bundle.getString("nome");
            textEmail = bundle.getString("email");
            foto = bundle.getBoolean("foto");
        }

        addInfoCard();
        addLoading();
        addDinamyccard();

        return view;
    }

    public void addDinamyccard() {
        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(180, TimeUnit.SECONDS)
                .writeTimeout(180, TimeUnit.SECONDS)
                .readTimeout(180, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit2 = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl("https://pi4facenac.azurewebsites.net/PI4/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiUsers apiUsers = retrofit2.create(ApiUsers.class);
        Call<ArrayList<GetFeed>> callPosts = apiUsers.getMyPosts(idPosts);

        Callback<ArrayList<GetFeed>> callbackPosts = new Callback<ArrayList<GetFeed>>() {
            @Override
            public void onResponse(Call<ArrayList<GetFeed>> call, Response<ArrayList<GetFeed>> response) {
                ArrayList<GetFeed> myposts = response.body();
                String nomeUser = "Sem nome";
                String dataUser = "Sem data";
                String dataTexto = "Sem texto";
                int dataNumCurtidas = 0;
                Long dataUsuario = Long.valueOf(0);
                String fotoUser = "";
                Long id = Long.valueOf(0);
                Boolean liked = false;


                if (response.code() == 200) {
                    myposts.size();

                    if (response.isSuccessful()) {
                        for (int i = 0; i < myposts.size(); i++) {
                            if (myposts.get(i).getNomeUser() != null) {
                                nomeUser = myposts.get(i).getNomeUser();
                            }
                            if (myposts.get(i).getData() != null){
                                dataUser = myposts.get(i).getData();
                            }
                            if (myposts.get(i).getTexto() != null){
                                dataTexto = myposts.get(i).getTexto();
                            }
                            if (myposts.get(i).getNumCurtidas() != null){
                                dataNumCurtidas = myposts.get(i).getNumCurtidas();
                            }
                            if (myposts.get(i).getUsuario() != null){
                                dataUsuario = myposts.get(i).getUsuario();
                            }
                            if (myposts.get(i).getFotoUser() != null){
                                fotoUser = myposts.get(i).getFotoUser();
                            }

                            if (myposts.get(i).getId() != null){
                                id = myposts.get(i).getId();
                            }

                            if (myposts.get(i).getLiked() != null){
                                liked = myposts.get(i).getLiked();
                            }
                            addCard(nomeUser, dataUser, dataTexto, dataNumCurtidas, dataUsuario,
                                    fotoUser, id, liked, myposts.get(i).getTemFoto());
                        }

                        progressBarLoading.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<GetFeed>> call, Throwable t) {
                t.printStackTrace();
            }
        };

        callPosts.enqueue(callbackPosts);
    }

    public void addCard(String nome, String data, String desc, Integer curtidas, Long usuario, String fotoUser, Long id, Boolean liked, Integer temFoto) {
        CardView cardView = (CardView) LayoutInflater.from(getActivity())
                .inflate(R.layout.card_feed, mensagens, false);

        nomeFeed = cardView.findViewById(R.id.nomeFeed);
        dataFeed = cardView.findViewById(R.id.dataFeed);
        descFeed = cardView.findViewById(R.id.descFeed);
        imageFeedDesc = cardView.findViewById(R.id.imageFeedDesc);
        imagemFeed = cardView.findViewById(R.id.imagemFeed);
        comentar = cardView.findViewById(R.id.comentar);
        idUser = cardView.findViewById(R.id.idUser);
        idHistorico = cardView.findViewById(R.id.idHistorico);

        final TextView curtidaFeed = cardView.findViewById(R.id.curtidaFeed);
        final Button curtir = cardView.findViewById(R.id.curtir);

        idUser.setText(String.valueOf(usuario));
        idHistorico.setText(String.valueOf(id));

        comentar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent comment = new Intent(getActivity(), FeedActivity.CommentActivity.class);
                startActivity(comment);
            }
        });

        if (liked) {
            curtir.setTextColor(getResources().getColor(R.color.bgbutton));
        }


        curtir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Retrofit retrofit2 = new Retrofit.Builder()
                        .baseUrl("https://pi4facenac.azurewebsites.net/PI4/api/curtida/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                Long idusr = Long.valueOf(idUser.getText().toString());
                Long idHist = Long.valueOf(idHistorico.getText().toString());

                ApiUsers apiUsers = retrofit2.create(ApiUsers.class);
                Call<Curtidas> comments = apiUsers.postComment(idusr, idHist);

                Callback<Curtidas> callback = new Callback<Curtidas>() {
                    @Override
                    public void onResponse(Call<Curtidas> call, Response<Curtidas> response) {
                        Curtidas body = response.body();

                        if (response.isSuccessful() && body != null) {
                            Long totalCurtida = body.getTotalCurtidas();
                            String numCurtidas = totalCurtida < 2 ? totalCurtida.toString()  + " like" : totalCurtida + "likes";
                            numCurtidas = totalCurtida == 0 ? "seja o primeiro a curtir" : numCurtidas;

                            curtidaFeed.setText(numCurtidas);

                            if (body.getStatus()) {
                                curtir.setTextColor(getResources().getColor(R.color.bgbutton));
                                Toast.makeText(getActivity(), "Curtido", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getActivity(), "Descurtido", Toast.LENGTH_LONG).show();
                                curtir.setTextColor(getResources().getColor(R.color.facesenac));
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Curtidas> call, Throwable t) {
                        t.printStackTrace();
                    }
                };

                comments.enqueue(callback);
            }
        });

        String curtidaTexto = Integer.toString(curtidas);

        String url = "https://pi4facenac.azurewebsites.net/PI4/api/users/image/" + usuario;
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(getActivity()));


         DisplayImageOptions optionsOne = new DisplayImageOptions.Builder()
         .showImageOnLoading(R.drawable.loading_icon)
         .showImageForEmptyUri(R.drawable.noimage)
         .showImageOnFail(R.drawable.noimage)
         .cacheInMemory(true)
         .cacheOnDisk(true).build();

        if (!fotoUser.equals("0")) {
            imageLoader.displayImage(url, imagemFeed, optionsOne);
        }

        if (curtidaTexto.equals("0")) {
            curtidaTexto = "seja o primeiro a curtir";
        } else {
            curtidaTexto = curtidaTexto.equals("1") ? curtidaTexto + " like" : curtidaTexto + " likes";
        }

        String urlPost = "https://pi4facenac.azurewebsites.net/PI4/api/posts/image/" + id;
        ImageLoader imageLoaderPost = ImageLoader.getInstance();
        imageLoaderPost.init(ImageLoaderConfiguration.createDefault(getActivity()));

        DisplayImageOptions options = new DisplayImageOptions.Builder()
        .showImageOnLoading(R.drawable.loading_icon)
        .showImageForEmptyUri(R.drawable.noimage)
        .showImageOnFail(R.drawable.noimage)
        .cacheInMemory(true)                                                                                    
        .cacheOnDisk(true).build();

        WindowManager wm = (WindowManager) getActivity().getSystemService(getActivity().WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);

        int largura = metrics.widthPixels;

        if (temFoto == 1) {
          imageLoaderPost.displayImage(urlPost, imageFeedDesc, options);

          imageFeedDesc.setVisibility(View.VISIBLE);
          imageFeedDesc.setMaxWidth(largura);
          imageFeedDesc.setMinimumWidth(largura);
        } else {
            imageFeedDesc.setVisibility(View.GONE);
        }

        String[] partialData = data.split("-");
        String modifiedData = partialData[2] + "/" + partialData[1] + "/" + partialData[0];

        nomeFeed.setText(nome);
        descFeed.setText(desc);
        dataFeed.setText(modifiedData);

        curtidaFeed.setText(curtidaTexto);
        mensagens.addView(cardView);
    }

    public void addInfoCard() {
        CardView cardView = (CardView) LayoutInflater.from(getActivity())
                .inflate(R.layout.card_profile, mensagens, false);

        TextView nomePerfil = cardView.findViewById(R.id.nomePerfil);
        TextView emailPerfil = cardView.findViewById(R.id.emailPerfil);
        ImageView imageProfile = cardView.findViewById(R.id.imagePefil);

        nomePerfil.setText(textnome);
        emailPerfil.setText(textEmail);

        String url = "https://pi4facenac.azurewebsites.net/PI4/api/users/image/" + idPosts;
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(getActivity()));

        if (foto) {
            imageLoader.displayImage(url, imageProfile);
        }

        mensagens.addView(cardView);
    }

    public void addLoading() {
        CardView cardView = (CardView) LayoutInflater.from(getActivity())
                .inflate(R.layout.loading, mensagens, false);

        progressBarLoading = cardView.findViewById(R.id.progressBarLoading);
        mensagens.addView(cardView);
    }
}


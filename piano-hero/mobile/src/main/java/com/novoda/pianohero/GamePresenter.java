package com.novoda.pianohero;

class GamePresenter implements GameMvp.Presenter {

    private final GameMvp.Model gameModel;
    private final GameMvp.View view;

    GamePresenter(GameMvp.Model gameModel, GameMvp.View view) {
        this.gameModel = gameModel;
        this.view = view;
    }

    @Override
    public void onCreate() {
        gameModel.startGame(andInformView);
    }

    private final GameMvp.Model.StartCallback andInformView = new GameMvp.Model.StartCallback() {
        @Override
        public void onGameStarted(RoundViewModel viewModel) {
            view.showRound(viewModel);
        }
    };

    @Override
    public void onNotePlayed(Note note) {
        gameModel.playGameRound(roundCallback, gameCompletionCallback, note);
    }

    private final GameMvp.Model.RoundCallback roundCallback = new GameMvp.Model.RoundCallback() {
        @Override
        public void onRoundUpdate(RoundViewModel viewModel) {
            view.showRound(viewModel);
        }
    };

    private final GameMvp.Model.CompletionCallback gameCompletionCallback = new GameMvp.Model.CompletionCallback() {
        @Override
        public void onGameComplete() {
            view.showGameComplete(new GameOverViewModel("game complete, another!"));
            gameModel.startGame(andInformView);
        }
    };

    @Override
    public void onRestartGameSelected() {
        gameModel.startGame(andInformView);
    }

}
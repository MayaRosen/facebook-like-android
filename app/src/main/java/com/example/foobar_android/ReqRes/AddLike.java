package com.example.foobar_android.ReqRes;

public class AddLike {
    private int addLike;
    public AddLike(int like){
        this.addLike = like;
    }

    public int getLike() {
        return addLike;
    }

    public void setLike(int addLike) {
        this.addLike = addLike;
    }
}

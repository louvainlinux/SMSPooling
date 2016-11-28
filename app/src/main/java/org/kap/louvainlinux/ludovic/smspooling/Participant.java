package org.kap.louvainlinux.ludovic.smspooling;

/**
 * Created by Ludovic Zipsin on 27/11/16.
 * Mail: ludovic.j.r.zipsin@gmail.com
 * Github: https://github.com/LudoZipsin
 */
public class Participant implements Comparable<Participant> {

    private String name;
    private int number;
    private int vote;
    private String pool;
    private int id;

    public Participant(String name, int number, int vote, String pool, int id){
        this.name = name;
        this.number = number;
        this.vote = vote;
        this.pool = pool;
        this.id = id;
    }

    public Participant(String name, String pool, int vote, int id){
        this.name = name;
        this.vote = vote;
        this.pool = pool;
        this.id = id;
    }

    public Participant(String name, int number, String pool, int id){
        this.name = name;
        this.number = number;
        this.pool = pool;
        this.vote = 0;
        this.id = id;
    }

    public Participant(String name, String pool){
        this.name = name;
        this.pool = pool;
        this.vote = 0;

    }

    public Participant(String name, String pool, int id){
        this.name = name;
        this.pool = pool;
        this.vote = 0;
        this.id = id;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getVote() {
        return vote;
    }

    public void setVote(int vote) {
        this.vote = vote;
    }

    public String getPool() {
        return pool;
    }

    public void setPool(String pool) {
        this.pool = pool;
    }



    public int compareTo(Participant participant){
        return participant.getVote()-getVote();
    }

}

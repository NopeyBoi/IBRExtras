package nopey.ibrextras.objects;

public class Record {
    private final String Name;
    private final int Position;
    private final String Time;
    private final int Finishes;
    private final int Attempts;
    private final String TotalTime;

    public Record(String name, int position, String time, int finishes, int attempts, String totalTime) {

        this.Name = name;
        this.Position = position;
        this.Time = time;
        this.Finishes = finishes;
        this.Attempts = attempts;
        this.TotalTime = totalTime;
    }

    public String getName() {
        return Name;
    }

    public int getPosition() {
        return Position;
    }

    public String getTime() {
        return Time;
    }

    public int getFinishes() {
        return Finishes;
    }

    public int getAttempts() {
        return Attempts;
    }

    public String getTotalTime() {
        return TotalTime;
    }

    public String toString() {
        return this.Name + " - Position: " + this.Position + " - Time: " + this.Time;
    }
}

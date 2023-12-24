import java.math.BigInteger;
import java.util.Arrays;

public class Day24 extends Solver {

    @Override
    // Got bunch of balls moving in certain speed, got to find how many of them hit each other
    public Object solve() {
        var map = getDataLines().stream().map(Hail::new).toArray(Hail[]::new);
        var sum = 0;
        for (int i = 0; i < map.length; i++) {
            var h1 = map[i];
            for (int j = i + 1; j < map.length; j++) {
                var h2 = map[j];
                var crossPoint = h1.linesIntersect(h2);

                if(crossPoint == null
                        || doesNotPass(crossPoint[0], crossPoint[1], h1)
                        || doesNotPass(crossPoint[0], crossPoint[1], h2)) {
                    continue;
                }

                if (crossPoint[0].compareTo(new BigInteger("200000000000000")) >= 0
                        && crossPoint[0].compareTo(new BigInteger("400000000000000")) <= 0
                        && crossPoint[1].compareTo(new BigInteger("200000000000000")) >= 0
                        && crossPoint[1].compareTo(new BigInteger("400000000000000")) <= 0) {
                    sum++;
                }
            }
        }

        return sum;
    }

    // need the delta and speed to be in the same direction, so it will happen, not has happened
    private boolean doesNotPass(BigInteger newX, BigInteger newY, Hail h) {
        var dx = newX.subtract(h.x);
        var dy = newY.subtract(h.y);

        return !((dx.compareTo(BigInteger.ZERO) > 0) == (h.vx.compareTo(BigInteger.ZERO) > 0)
                && (dy.compareTo(BigInteger.ZERO) > 0) == (h.vy.compareTo(BigInteger.ZERO) > 0));
    }

    @Override
    // Got to figure out from what position in what velocity to throw my ball to hit all of the others
    // after hitting it doesn't change trajectory or slow down
    // honest to god have no idea how to solve this, so just print this in wolfram matematica, it's got a 15 day free trial
    public Object solve2() {
        var map = getDataLines().stream().map(Hail::new).toArray(Hail[]::new);

        var sb = new StringBuilder();
        sb.append("Solve[{");
        for (int j = 0; j < 5; j++) {
            var d = map[j];
            var t = "t" + j;
            sb.append(t).append(" >= 0, ");
            sb.append(String.format("%s + %s * %s == px + vx * %s, ",d.x, d.vx, t, t));
            sb.append(String.format("%s + %s * %s == py + vy * %s, ",d.y, d.vy, t, t));
            sb.append(String.format("%s + %s * %s == pz + vz * %s",d.z, d.vz, t, t));
            if (j < 4) {
                sb.append(", ");
            }
        }

        sb.append("}, {px,py,pz,vx,vy,vz}, Integers]");
        return sb.toString();
    }
}

class Hail {
    public Hail (String line) {
        var posAndSpeed = line.split("@");
        var pos = Arrays.stream(posAndSpeed[0].split(","))
                .map(String::trim)
                .filter(x -> !x.isEmpty())
                .map(BigInteger::new)
                .toArray(BigInteger[]::new);
        x = pos[0];
        y = pos[1];
        z = pos[2];

        var speed = Arrays.stream(posAndSpeed[1].split(","))
                .map(String::trim)
                .filter(x -> !x.isEmpty())
                .map(BigInteger::new)
                .toArray(BigInteger[]::new);

        vx = speed[0];
        vy = speed[1];
        vz = speed[2];
    }

    public BigInteger x;
    public BigInteger y;
    public BigInteger z;
    public BigInteger vx;
    public BigInteger vy;
    public BigInteger vz;

    public BigInteger[] linesIntersect(Hail b) {
        var line1 = getLine();
        var line2 = b.getLine();
        var xdiff = new BigInteger[] { line1[0][0].subtract(line1[1][0]), line2[0][0].subtract(line2[1][0])  };
        var ydiff = new BigInteger[] { line1[0][1].subtract(line1[1][1]), line2[0][1].subtract(line2[1][1])  };

        var div = det(xdiff, ydiff);

        if (div.compareTo(BigInteger.ZERO) == 0) {
            return null;
        }
        var d = new BigInteger [] {det(line1[0], line1[1]), det(line2[0], line2[1])};
        var xr = det(d, xdiff).divide(div);
        var yr = det(d, ydiff).divide(div);
        return new BigInteger[] {xr, yr};
    }

    private BigInteger det(BigInteger[] line1, BigInteger[] line2) {
        return line1[0].multiply(line2[1]).subtract(line1[1].multiply(line2[0]));
    }

    public BigInteger[][] getLine() {
        return new BigInteger[][] {{x, y, z}, {x.add(vx), y.add(vy), z.add(vz)}};
    }
}
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

import parcs.*;

public class PollardTask implements AM {
    public void run(AMInfo info) {
        ArrayList<Integer> data = (ArrayList<Integer>) info.parent.readObject();
        int CurWorkerIndex = data.get(0);
        int NumOfWorkers = data.get(1);
        int NumOfOperations = data.get(2);

        StringBuilder finalResult = new StringBuilder();

        for (int i = CurWorkerIndex + 1; i < NumOfOperations + 1; i += NumOfWorkers) {
            ArrayList<Integer> result = SimpleFactorisation(i);
            finalResult.append(i + " | ");
            for (int j : result)
            {
                finalResult.append(j + " ");
            }
            finalResult.append("\n");
        }

        info.parent.write(finalResult);
    }

    private static ArrayList<Integer> SimpleFactorisation(int num) {
        ArrayList<Integer> result = new ArrayList<>();
        int d = 1;
        while (num != 1)
        {
            d = 1;
            for (int i = 2; i < Math.sqrt(num) + 1; i++)
            {
                if (num % i == 0)
                {
                    d = i;
                    break;
                }
            }
            if (d == 1)
            {
                result.add(num);
                break;
            }
            else
            {
                result.add(d);
                num = num / d;
            }
        }
        return result;
    }

    private static ArrayList<Integer> FullPollard(int num) {
        ArrayList<Integer> result = new ArrayList<>();
        int res = 1;
        while (res != num)
        {
            num = num / res;
            res = Pollard(num);
            if (MillerRabin(res, 100) == true)
            {
                result.add(res);
            }
            else
            {
                ArrayList<Integer> dlcres = FullPollard(res);
                for (int n : dlcres)
                {
                    result.add(n);
                }
            }
        }
        return result;
    }

    private static int Pollard(int num) {
        Random random = new Random();
        int x = 1 + random.nextInt(num - 1);

        if (x > num - 2)
        {
            x = x % (num - 2);
        }

        int stage = 2;
        int y = 1;
        int i = 0;

        while (GCD(num, Math.abs(x - y)) == 1)
        {
            if (stage == i)
            {
                y = x;
                stage = stage * 2;
            }
            x = (x * x + 1) % num;
            i = i + 1;
        }

        return GCD(num, Math.abs(x - y));
    }

    private static int GCD(int numA, int numB) {
        int fakenum;
        while (numB != 0)
        {
            fakenum = numB;
            numB = (numA % numB);
            numA = fakenum;
        }

        int result = numA;

        return result;
    }

    private static boolean MillerRabin(int num, int k) {
        if (num == 2)
        {
            return true;
        }

        if (num % 2 == 0 || num <= 1) {
            return false;
        }

        int m = (num - 1) / 2;
        int t = 1;
        while (m % 2 == 0) {
            m /= 2;
            t++;
        }

        Random random = new Random();

        for (int i = 1; i <= k; i++) {
            int a = 1 + random.nextInt(num - 1);
            t++;
            int u = modPow(a, m, num);

            if (u != 1 && u != num - 1) {
                int j = 1;
                boolean isSkip = false;

                while (u != -1 && j < t) {
                    u = (u * u) % num;
                    j++;
                    if (u == 1) {
                        return false;
                    }
                    if (u == num - 1) {
                        isSkip = true;
                        break;
                    }
                }

                if (isSkip) {
                    continue;
                }
                if (u != -1) {
                    return false;
                }
            }
        }
        return true;
    }

    private static int modPow(int base, int exp, int mod) {
        int result = 1;
        base = base % mod;

        while (exp > 0) {
            if ((exp & 1) == 1) {
                result = (result * base) % mod;
            }
            exp >>= 1;
            base = (base * base) % mod;
        }

        return result;
    }
}

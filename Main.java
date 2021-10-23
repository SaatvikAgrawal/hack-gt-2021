import java.util.*;

class Main {
  /*
  After looking at this, I think a lot of ideas can be grouped together
  */
  public static void main(String[] args) {
    System.out.println("Hello world!");
    Main main = new Main();
    System.out.println(main.split(3, 2));
    String[] arr = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};
    int numTeams = 5;
    /*ArrayList<ArrayList<String>> miles = main.splitTeamsRandom(arr, numTeams); 
    
    miles = main.milesRandomAttempt(arr, numTeams);*/
    String[] names = {"Richard Obama","Joe Roosevelt","Donald Johnson","Brick Washington","Richard Ali","Dan Roosevelt","Joe Smith","Bill Obama","John Ali","Frank Washington","John Smith","Joe Ali","Jack Obama","Joe Roosevelt","Brick Obama"};

    int[][] values = {{57,0,71,4,35},{4,45,63,71,47},{74,94,8,15,28},{11,28,19,81,72},{8,2,46,4,12},{8,1,8,76,76},{69,19,96,25,97},{90,53,28,35,34},{44,96,36,49,92},{80,80,25,49,74},{44,29,4,82,57},{31,4,27,7,96},{36,56,22,100,95},{47,64,98,25,93},{88,63,90,90,21}};
    Person[] people = new Person[15];
    for (int i = 0; i < 15; i++) {
      people[i] = new Person(names[i], values[i][0], values[i][1], values[i][2], values[i][3], values[i][4]);
    }

/*
    ArrayList<ArrayList<Person>> bestTeams = new ArrayList<ArrayList<Person>>();
    int bestFitness = Integer.MIN_VALUE;

    for (int i = 0; i < 100000; i++) {
      ArrayList<ArrayList<Person>> teams = main.milesRandomAttempt(people, 5);
      int fitness = main.fitness(teams);
      if (fitness > bestFitness) {
          bestTeams = teams;
          bestFitness = fitness;
      }
      System.out.println(bestFitness);
    }
    for (int i = 0; i < 5; i++) {
      System.out.print(bestTeams.get(i) + " ");
    }
    double[] splitTime = main.splitTime(3, 20, 10);
    for (int i = 0; i < splitTime.length; ++i) {
      System.out.printf("%.2f ", splitTime[i]);
    }
    System.out.println();
    */
    int iters = 0;
    ArrayList<ArrayList<ArrayList<Person>>> creations = new ArrayList<>();
    while(iters < 500) {
      creations.add(main.milesRandomAttempt(people, 5));
      int s = creations.size();
      for(int x = 0; x < s - 1; x++) {
        for(int y = x + 1; y < s; y++) {
          creations.add(reproduce(creations.get(x), creations.get(y)));
        }
      }
      creations.sort(Comparator.comparingInt(x -> fitness(x)));
      creations.subList(Math.max(0, creations.size() - 50), creations.size());
      iters++;
    }
    System.out.println(creations.get(creations.size() - 1));
  }

  public double split(int price, int people) {
    return 1.0 * price / people;
  }

  public ArrayList<ArrayList<String>> splitTeamsRandom(String[] arr, int numTeams) {
    ArrayList<Integer> teams = new ArrayList<Integer>(arr.length);
    ArrayList<ArrayList<String>> out = new ArrayList<ArrayList<String>>();
    for (int i = 0; i < numTeams; ++i) {
      out.add(new ArrayList<String>());
    }
    for (int i = 0; i < numTeams; ++i) {
      for (int j = 0; j < arr.length / numTeams; ++j) {
        teams.add(i);
      }
    }
    for (int i = 0; i < arr.length % numTeams; ++i) {
      teams.add(i); 
    }
    for (int i = 0; i < arr.length; ++i) {
      int random = (int)(Math.random() * teams.size());
      int team = teams.remove(random);
      out.get(team).add(arr[i]);
    }
    return out;
  }
  
  // Splits given amount of time among a given amout of tasks with added variation if necessary
  // inputs: tasks = # of tasks, time = amount of time, variation (scale of 1 - 10) of how much variation is desired
  // outputs: double[] time designated per task
  public double[] splitTime(int tasks, double time, int variation) {
    // Variation is on a scale of 0 - 10;
    // The variation factor will determine the amount of variation in results per task
    double equal = 1.0 * time / tasks;
    int actualVariation = (int) (time * variation / 7);
    double[] var = new double[tasks];
    // make list to keep track of variation, must add to 0;
    for (int i = 0; i < tasks; ++i) {
      var[i] = equal;
    }
    for (int i = 0; i < actualVariation; ++i) {
      // for every iteration, we take some 
      double ran = Math.random();
      int index = (int) (Math.random() * tasks);

      int index2 = (int) (Math.random() * tasks);
      if (var[index2] - ran > 0 && var[index] + ran < time) {
        var[index2] -= ran;
        var[index] += ran;
      }
    }
    return var;
  }
  

  public static ArrayList<ArrayList<Person>> milesRandomAttempt(Person[] arr, int numTeams) {
    ArrayList<ArrayList<Person>> ret = new ArrayList<>();
    HashSet<Integer> used = new HashSet<>();
    int peeps = arr.length;
    int temp = numTeams;
    for(int t = 0; t < numTeams; t++) {
      ret.add(new ArrayList<>());
      for(int x = 0; x < peeps / temp; x++) {
        int r;
        do {
          r = (int)(Math.random() * arr.length);
        } while (used.contains(r));
        ret.get(t).add(arr[r]);
        used.add(r);
      }
      peeps -= peeps / temp;
      temp--;
    }
    return ret;
  }

  public static int fitness(ArrayList<ArrayList<Person>> teams) {
    int pen = 0;
    for(ArrayList<Person> team : teams) {
      int averagei = 0;
      int averagea = 0;
      int averagen = 0;
      int averagec = 0;
      int averageo = 0;
      for(int x = 0; x < team.size(); x++) {
        averagei += team.get(x).introversion;
        averagea += team.get(x).agreeableness;
        averagen += team.get(x).neuroticism;
        averagec += team.get(x).conscientiousness;
        averageo += team.get(x).openness;
      }
      averagei /= team.size();
      averagea /= team.size();
      averagen /= team.size();
      averagec /= team.size();
      averageo /= team.size();
      for(int x = 0; x < team.size(); x++) {
        pen -= Math.abs(team.get(x).introversion - averagei);
        pen -= Math.abs(team.get(x).agreeableness - averagea);
        pen -= Math.abs(team.get(x).neuroticism - averagen);
        pen -= Math.abs(team.get(x).conscientiousness - averagec);
        pen -= Math.abs(team.get(x).openness - averageo);
      }
    }
    return pen;
  }

This package implements HBSS (Heuristic Biased Stochastic Search) for package EOSschedule with weights chosen accoring to a contention measure similar to that described in "Planning and Scheduling for Fleets of Earth Observing Satellites," Jeremy Frank, Ari Jonsson, Robert Morris, and Devid Smith, Proceedings of the 6th International Symposium on Artificial Intelligence, Robotis, Automation and Space 2002, Montreal, 18-22 June 2002.  The biggest change is that instead of using the contention alone as the weight, the weigth of each observation is a weighted sum of the contention and priority of the observation.  According to Robert Morris, the priority needs additional emphasis to get good schedules. Sensor availability and SSR limits are considered when calculating the contention.  They are combined in a weighted sum where the weights sum to 1.

One change in terminology: task contention is called difficulty, ala David Smith 'Notes on SSR Contention.'  This should reduce confusion.

Dependecies for updates, intialization (IMPORTANT):
    access windows
        number for task -> task need 
            sum of task need with contending windows -> access window contention
                minimum in task -> task difficulty

Initialization:
    create TaskList and AccessWindowList(s)
    generate sensorAvailContenders and SSRcontenders
    initial value calculations
        gen. TaskWeight needs
        gen. AccessWindowWeight contention
        gen. AccessWindowList weight sums
        gen. TaskWeight difficulty
        gen. TaskList weight sums

The next task to schedule is chosen by weighted roulette wheel with bias towards largest weight
The accessWindow for that task is chosen by weighted roulette wheel with bias toward smallest weight

When chosen, thee accessWindow is checked to make sure it really fits (dutyCyle could cause problems)
    DEBUG: with no duty cycle, should always fit
    if fits AccessWindow becomes SCHEDULED

SSR data structure is set up just before first schedule.  Also, the SSRNode must be changed with every new schedule
since the SSRNodes in the SSRtimeline are regenerated for each schedule.

NOTE: 
    numberOfAccessWindows,SSR available -> task need
    task need -> accessWindow contention
    accessWindow contention -> task difficulty

Note that an AW can become unschedulable for four reasons:
    1. The Scheduler determines that it violates a constraint.
    2. It's on the SensorAvailContenders for an AW that becomes SCHEDULED
    3. The SSR runs too low for it
    4. Another AW in it's task becomes SCHEDULED

Basic idea behind update:

For each thing that must be removed from contention data structure:
    - remove from lists
    - update immediate neighbors

When updated:
    - note the changes, but don't make them (unless it doesn't matter to anything else)
    - put self on one of the UpdateAndPropagate lists

Update and propagate in correct order:
    - Need (first tasks, then SSRcontenders needSum)
    - Contention, AccessWindowWeight
    - Difficulty
    - TaskWeight
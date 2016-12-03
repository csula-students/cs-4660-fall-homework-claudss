import sys
import math



#this is our board state array - it's empty for now but we put it all the way in the beginning so we can use it in the minimaxing function!
board= {}


#minimax function: takes starting coordinates as input
def minimaxer(pos):
    
    #nested structure: stores player positions at iterations of the algorithm
    graphs = {i: {} for i in range(n_players)}
    
    #copy of our board structure 
    graphset = set(x for x in board)
    
    #who is playing and in what order? this is the order of player outcomes we will consider.
    playing = list(range(my_id, n_players)) + list(range(0, my_id))
    
    #start at iteration 1.
    it = 1
    
    #loop until we can't get any changes anymore
    while True:
        # this determines our loop. we change it if we find somewhere to go
        full = True
        
        #possible moves 
        moves = {}
        
        #for each player:
        for p in playing:
            
            # go through the possible positions they can start off the minimax algorithm evaluation (only 1 to start in iteration 1)
            for x in pos[p]:
                
                #for neighbors:
                for n in foundneighbors[x]:
                    
                    #if we don't have a starting position or an already-visited position:
                    if n not in graphset or (n in moves and it == 1):
                        #we can add another possibility! full is false for now...
                        full = False
                        
                        # add this position to our ~visited~ list
                        graphset.add(n)
                        
                        #add this position to the list of moves
                        moves[n] = p
                        
                        
        for k, v in moves.items():
            graphs[v][k] = it
            
        # full refers to our collection of possible states. if we haven't found a new one it remains true and it means we're done
        if full:
            break
        
        # update the starting positions we will use via the moves we have determined! this way we see all possible choices
        pos = [[k for k, v in moves.items() if v == i] for i in range(n_players)]
        
        #increase iteration number
        it += 1
        
    #this is how many tiles we (the player!) can get to.. it needs to be higher
    mine = len(graphs[my_id])
    
    #this is how many tiles all our enemies can get to... the lower it is the better off we are!
    theirs = sum([len(graphs[i]) for i in range(n_players) if i != my_id])
    
    
    #how much our enemies have to travel
    attackdistance = sum([sum(graphs[i].values()) for i in range(n_players) if i != my_id])
    
    #kind of simple weighting to just 
    return sum([mine * 10000000, theirs * -100000, attackdistance])

# here we create a kind of structure to hold neighbours. makes it easy to access where we can go at any given time!
foundneighbors = {}
for i in range(30):
    for j in range(20):
        neighbors = []
        if i < 29:
            neighbors.append((i + 1, j))
        if i > 0:
            neighbors.append((i - 1, j))
        if j < 19:
            neighbors.append((i, j + 1))
        if j > 0:
            neighbors.append((i, j - 1))
        foundneighbors[(i, j)] = neighbors
        

#this looks at our old position vs the one we want to go to so we know what to print
#old and new are coordinate arrays: for example if we had old = [0 1] and new = [1 1], then old[0]<new[0] and we would turn RIGHT (increase x)
def direction(old, new):
    if old[0] < new[0]:
        return "RIGHT"
    if old[1] < new[1]:
        return "DOWN"
    if old[0] > new[0]:
        return "LEFT"
    return "UP"



#BASE LOOP: fulfills all game actions
while True:
    # reads in input and determines what number we are
    n_players, my_id = [int(i) for i in input().split()]
    
    #MONITORS ALL MOVEMENTS
    curr_moves = []
    
    
    #base loop: checks up on all players
    for i in range(n_players):
        #get in our starting and upcoming coordinates
        x0, y0, x1, y1 = [int(j) for j in input().split()]
        
        #MARK ALL POSITIONS ON THE BOARD ARRAY
        board[(x0, y0)] = i
        board[(x1, y1)] = i
        
        #add moves to our current moves array
        curr_moves.append((x1, y1))
        
    #we iterate through each player and file their movements away
    for p in range(n_players):
        x1, y1 = curr_moves[p]
        
        #let's look at where we (and only we) want to go
        if p == my_id:
            
            #this is where we are
            me = (x1, y1)
            
            #scores array to put all our minimax possibilities into
            scores = []
            
            #now we go through the neighbors of our location
            for n in foundneighbors[me]:
                
                #if the neighbor isn't already visited:
                if n not in board:
                    
                    
                    start = [[x] for x in curr_moves.copy()]
                    
                    #consider our neighbor to be a ~starting position~ for us the player
                    start[my_id] = [n]

                    #feed this starting position into minimaxer
                    score = minimaxer(start)
                    
                    #add the resulting score to our big list
                    scores.append((score, n))
                    
    # we sort all our scores using python's sorted() but then we reverse the list so that its first member is the highest and thus best score for us!
    bestmove = sorted(scores, key=lambda x: x[0], reverse=True)[0]
    
    #make our move based on the best possible choice
    print(direction(me, bestmove[-1]))


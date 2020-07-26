%% @author ankuraggarwal
%% @doc @todo Add description to test2.


-module(exchange).

%% ====================================================================
%% API functions
%% ====================================================================
-export([start/0]).



%% ====================================================================
%% Internal functions
%% ====================================================================

start()->
io:format("~n~n** calls to be made **~n~n",[]),
   
    {ok, Data} = file:consult("calls.txt"),
    C_dict = maps:from_list(Data),
   Create_Threads = fun(Key, Value, Acc)->
								   io:format("~w: ~w ~n",[Key, Value]),
								   Process = spawn(calling,main, [Key, Value,self()]),	
			                       register(Key,Process)
								   end,
   maps:fold(Create_Threads, [], C_dict),
   io:format("~n",[]),
   print_all().



print_all()->
    receive
		{intro_Message,Name,Reciver,Mins} ->
            io:fwrite("~w received intro message from ~w [~w]~n",[Name,Reciver,Mins]),
           print_all();
		{reply_Message,Name,Reciver,Mins} ->
            io:fwrite("~w received reply message from ~w [~w]~n",[Name,Reciver,Mins]),
            print_all()
    after
        10000->
		%%io:format("~n",[]),
		io:fwrite("~nMaster has received no replies for 10 seconds, ending...~n",[])
    end.


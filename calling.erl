%% @author ankuraggarwal
%% @doc @todo Add description to test3.


-module(calling).

%% ====================================================================
%% API functions
%% ====================================================================
-export([main/3]).



%% ====================================================================
%% Internal functions
%% ====================================================================



   
call_message (Name,Name_List,Pid) ->
	     
	            lists:foreach(fun(Receiver) -> whereis(Receiver)!{Name,intro,Pid} 
										end, 
							  Name_List).
	
recive_message(Name) ->
	receive
		                {Recivers,intro,Pid} -> 
							Time = element(3,now()),
							timer:sleep(round(timer:seconds(0.01*(rand:uniform())))),
							Pid!{intro_Message,Name,Recivers,Time},
		                    whereis(Recivers)!{reply_message,Name,Time,Pid},
		                    recive_message(Name);
		
		                {reply_message,Recivers,Mins,Pid} ->
							Pid!{reply_Message,Name,Recivers,Mins},
		                    recive_message(Name)
	            after
	                5000->
					    
						io:fwrite("~nProcess ~w has received no calls for 5 second, ending...~n",[Name])

	            end.

main(Name,Name_List,Pid)->
	timer:sleep(100),
	call_message (Name,Name_List,Pid),
	%%io:fwrite(Name),
	%%io:fwrite("~n~n"),
    recive_message(Name).
  
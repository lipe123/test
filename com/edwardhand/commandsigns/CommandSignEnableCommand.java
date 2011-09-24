package com.edwardhand.commandsigns;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.omggamingonline.minecraft.common.IsDigits;
import com.omggamingonline.minecraft.common.commands.BaseCommand;

public class CommandSignEnableCommand extends BaseCommand {

	private CommandSigns _plugin;
	
	public CommandSignEnableCommand(CommandSigns plugin)
	{
		_plugin = plugin;
		
		this.usage = "/commandsign enable [<uses>] [[<x> <y> <z> [[w:]<world>]] | [[a:]alias]]";
		this.minArgs = 0;
		this.maxArgs = 5;
		this.identifiers.add("commandsign enable");
	}
	
	@Override
	public void execute(CommandSender commandSender, String[] args) 
	{
		switch(args.length)
		{
		case 0: //Right-click enable mode
			{
				if(commandSender instanceof Player)
				{
					Player player = (Player)commandSender;
					_plugin.getPlayerModes().put(player.getName(), "E");
                    player.sendMessage(_plugin.getLocalizedStrings().getLocalizedString("EnableWandEnabled"));
				}
				else
				{
					commandSender.sendMessage("CommandSigns: The console cannot use this command.");
				}
			}
			break;
		case 1: //Right-click enable mode with uses
			{
				if(IsDigits.check(args[0]))
				{
	                //assumed to be /cs enable [uses]
				    if(commandSender instanceof Player)
    				{
    					Player player = (Player)commandSender;
    					_plugin.getPlayerModes().put(player.getName(), "E:" + args[0]);
                        player.sendMessage(String.format(_plugin.getLocalizedStrings().getLocalizedString("EnableWithUsesWandEnabled"),args[0]));
    				}
    				else
    				{
    					commandSender.sendMessage("CommandSigns: The console cannot use this command.");
    				}
				}
				else
				{
	                //assumed to be /cs enable alias
				    args[0] = args[0].toLowerCase();
				    if(args[0].startsWith("a:"))
				        args[0] = args[0].substring(2);
	                Location loc = _plugin.getAliases().getLocation(args[0]);
	                
	                if(loc != null)
	                {
	                    Block block = loc.getBlock();
	                    if(block != null && _plugin.verifyDisabledCommandSign(block))
	                    {
	                        if((commandSender instanceof Player && _plugin.hasRequiredPermissionsToEnable(block, (Player)commandSender))
	                                || !(commandSender instanceof Player))
	                        {
	                            _plugin.enableSign(block);
	                            commandSender.sendMessage(_plugin.getLocalizedStrings().getLocalizedString("CommandSignEnabled"));
	                        }
	                        else
	                        {
	                            commandSender.sendMessage(_plugin.getLocalizedStrings().getLocalizedString("CommandSignNoPermissionToEnable"));
	                        }
	                    }
	                }
				}
			}
			break;
		case 2:
    		{
                //assumed to be /cs enable uses alias
                args[1] = args[1].toLowerCase();
                if(args[1].startsWith("a:"))
                    args[1] = args[1].substring(2);
                Location loc = _plugin.getAliases().getLocation(args[1]);
                
                if(IsDigits.check(args[0]) && loc != null)
                {
                    Block block = loc.getBlock();
                    if(block != null && _plugin.verifyDisabledCommandSign(block))
                    {
                        if((commandSender instanceof Player && _plugin.hasRequiredPermissionsToEnable(block, (Player)commandSender))
                                || !(commandSender instanceof Player))
                        {
                            _plugin.enableSign(block, Integer.parseInt(args[0]));
                            commandSender.sendMessage(_plugin.getLocalizedStrings().getLocalizedString("CommandSignEnabled"));
                        }
                        else
                        {
                            commandSender.sendMessage(_plugin.getLocalizedStrings().getLocalizedString("CommandSignNoPermissionToEnable"));
                        }
                    }
                }
    		}
    		break;
		case 3: //Enable sign at position
			{
				//assumed to be /cs enable X Y Z
				World world = null;
				
				if(commandSender instanceof Player)
					world = ((Player)commandSender).getWorld();
				else
					world = _plugin.getServer().getWorlds().get(0);
				
				if(IsDigits.check(args[0])
						&& IsDigits.check(args[1])
						&& IsDigits.check(args[2]))
				{
					Block block = world.getBlockAt(Integer.parseInt(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2]));
					if(block != null && _plugin.verifyDisabledCommandSign(block))
					{
						if((commandSender instanceof Player && _plugin.hasRequiredPermissionsToEnable(block, (Player)commandSender))
								|| !(commandSender instanceof Player))
						{
							_plugin.enableSign(block);
							commandSender.sendMessage(_plugin.getLocalizedStrings().getLocalizedString("CommandSignEnabled"));
						}
						else
						{
							commandSender.sendMessage(_plugin.getLocalizedStrings().getLocalizedString("CommandSignNoPermissionToEnable"));
						}
					}
				}
			}
			break;
		case 4: //Enable sign at position with uses, or enable sign at position in world
			{
				if(!IsDigits.check(args[3]))
				{
					//assumed to be /cs enable X Y Z WORLD
					
					//remove world prefix in case of numeric worlds
					if(args[3].startsWith("w:"))
						args[3] = args[3].substring(2);
					
					World world = _plugin.getServer().getWorld(args[3]);
					if(world != null)
					{
						if(IsDigits.check(args[0])
								&& IsDigits.check(args[1])
								&& IsDigits.check(args[2]))
						{
							Block block = world.getBlockAt(Integer.parseInt(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2]));
							if(block != null && _plugin.verifyDisabledCommandSign(block))
							{
								if((commandSender instanceof Player && _plugin.hasRequiredPermissionsToEnable(block, (Player)commandSender))
										|| !(commandSender instanceof Player))
								{
									_plugin.enableSign(block);
									commandSender.sendMessage(_plugin.getLocalizedStrings().getLocalizedString("CommandSignEnabled"));
								}
								else
								{
									commandSender.sendMessage(_plugin.getLocalizedStrings().getLocalizedString("CommandSignNoPermissionToEnable"));
								}
							}
						}
					}
					else
					{
						commandSender.sendMessage(String.format(_plugin.getLocalizedStrings().getLocalizedString("WorldNotFound"), args[3]));
					}
				}
				else
				{
					//assumed to be /cs enable USES X Y Z
					if(commandSender instanceof Player
							&& IsDigits.check(args[0])
							&& IsDigits.check(args[1])
							&& IsDigits.check(args[2])
							&& IsDigits.check(args[3]))
					{
						Block block = ((Player)commandSender).getWorld().getBlockAt(Integer.parseInt(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3]));
						if(block != null && (_plugin.verifyDisabledCommandSign(block) || _plugin.verifyEnabledCommandSign(block)))
						{
							if((commandSender instanceof Player && _plugin.hasRequiredPermissionsToEnable(block, (Player)commandSender))
									|| !(commandSender instanceof Player))
							{
								_plugin.disableSign(block);
								_plugin.enableSign(block, Integer.parseInt(args[0]));
								commandSender.sendMessage(String.format(_plugin.getLocalizedStrings().getLocalizedString("EnableWithUsesWandEnabled"),args[0]));
							}
							else
							{
								commandSender.sendMessage(_plugin.getLocalizedStrings().getLocalizedString("CommandSignNoPermissionToEnable"));
							}
						}
					}
				}
			}
			break;
		case 5: //Enable sign at position in world with uses
			{
				//assumed to be /cs enable USES X Y Z WORLD
				
				//remove world prefix in case of numeric worlds
				if(args[4].startsWith("w:"))
					args[4] = args[4].substring(2);
				
				World world = _plugin.getServer().getWorld(args[4]);
				if(world != null)
				{
					if(IsDigits.check(args[0])
							&& IsDigits.check(args[1])
							&& IsDigits.check(args[2])
							&& IsDigits.check(args[3]))
					{
						Block block = world.getBlockAt(Integer.parseInt(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3]));
						if(block != null && (_plugin.verifyDisabledCommandSign(block) || _plugin.verifyEnabledCommandSign(block)))
						{
							if((commandSender instanceof Player && _plugin.hasRequiredPermissionsToEnable(block, (Player)commandSender))
									|| !(commandSender instanceof Player))
							{
								_plugin.disableSign(block);
								_plugin.enableSign(block, Integer.parseInt(args[0]));
								commandSender.sendMessage(String.format(_plugin.getLocalizedStrings().getLocalizedString("EnableWithUsesWandEnabled"),args[0]));
							}
							else
							{
								commandSender.sendMessage(_plugin.getLocalizedStrings().getLocalizedString("CommandSignNoPermissionToEnable"));
							}
						}
					}
				}
				else
				{
					commandSender.sendMessage(String.format(_plugin.getLocalizedStrings().getLocalizedString("WorldNotFound"), args[3]));
				}
			}
			break;
		default:
			{
				_plugin.log.info("? CommandSignsWarning: SignEnable default case.");
			}
			break;
		}
	}

}

display.setStatusBar( display.DefaultStatusBar )

local widget = require "widget"
local composer = require "composer"

local function onFirstView( event )
	composer.gotoScene( "view1" )
end

onFirstView()

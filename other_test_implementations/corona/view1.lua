local composer = require( "composer" )
local widget = require( "widget" )
local scene = composer.newScene()
local audio = require("audio")

local content = display.newText({
	text = "",
	x = display.contentWidth / 2,
	y = display.contentHeight / 3.5
})
content:setFillColor(0)

local progress = widget.newProgressView({
	width = display.contentWidth / 10 * 8,
	x = display.contentWidth / 2,
	y = 10,
	isAnimated = true
})

local function ackermann (m, n)
	if m == 0 then
		return n + 1
	else
		if n == 0 then
			return ackermann(m - 1, 1)
		else
			return ackermann(m - 1, ackermann(m, n - 1))
		end
	end
end

local function getTime()
  return os.time()
end

local function writeFile(text)
  local path = system.pathForFile("file.txt", system.DocumentsDirectory)
  local file, err = io.open(path, "w")
  if not file then
    print(err)
  else
    file:write(text)
    io.close(file)
  end
  file = nil
end

local function readFile()
  local path = system.pathForFile("file.txt", system.DocumentsDirectory)
  local file, err = io.open(path, "r")
  if not file then
    print(err)
  else
    local con = file:read("*a")
    io.close(file)
  end
  file = nil
end

local function runTests ()
  local f = {}
	progress:setProgress(0)
   for i = 1, 20 do
    local tn = getTime()
    
    local es = audio.loadSound( "s.mp3")
    audio.play(es)
    
    f[i] = (getTime() - tn)
    
    print(i .. " - " .. f[i])
		progress:setProgress(i / 20)
	
	end
  
  local text = ""
  local min = 9999
  local max = 0 
  local med = 0
  local avg = 0
  
  for i = 1, 20 do
    avg = avg + f[i]
    if f[i] > max then
      max = f[i]
    elseif f[i] < min then
      min = f[i]
    end
  end
  avg = avg / 20
  content.text = min .. " - " .. max .. " - " .. avg .. " - " .. med .. "\n"
	--coroutine.yield()
end

local function onRunTestsEvent (event)
	--timer.performWithDelay(1, coroutine.wrap(runTests), 101)
  runTests()
end

function scene:create( event )
	local sceneGroup = self.view

	local background = display.newRect(display.contentCenterX, display.contentCenterY, display.contentWidth, display.contentHeight)
	background:setFillColor(1)
	
	local title = display.newText("Corona Application", display.contentCenterX, 50, native.systemFont, 32)
	title:setFillColor(0);

	local button = widget.newButton({
		label = "Lol",
		x = display.contentCenterX,
		y = 90,
		onPress = onRunTestsEvent
	})

	sceneGroup:insert( background )
	sceneGroup:insert( title )
	sceneGroup:insert( content )
end

function scene:show( event )
	local sceneGroup = self.view
	local phase = event.phase
	
	if phase == "will" then
		-- Called when the scene is still off screen and is about to move on screen
	elseif phase == "did" then
		-- Called when the scene is now on screen
		-- 
		-- INSERT code here to make the scene come alive
		-- e.g. start timers, begin animation, play audio, etc.
	end	
end

function scene:hide( event )
	local sceneGroup = self.view
	local phase = event.phase
	
	if event.phase == "will" then
		-- Called when the scene is on screen and is about to move off screen
		--
		-- INSERT code here to pause the scene
		-- e.g. stop timers, stop animation, unload sounds, etc.)
	elseif phase == "did" then
		-- Called when the scene is now off screen
	end
end

function scene:destroy( event )
	local sceneGroup = self.view
	
	-- Called prior to the removal of scene's "view" (sceneGroup)
	-- 
	-- INSERT code here to cleanup the scene
	-- e.g. remove display objects, remove touch listeners, save state, etc.
end

---------------------------------------------------------------------------------

-- Listener setup
scene:addEventListener( "create", scene )
scene:addEventListener( "show", scene )
scene:addEventListener( "hide", scene )
scene:addEventListener( "destroy", scene )

-----------------------------------------------------------------------------------------

return scene
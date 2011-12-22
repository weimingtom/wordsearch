local head = [[
]]

local head2 = [[
]]

local tail = [[
]]

local fin = assert(io.open("edict.txt", "rb"))
s = fin:read("*a")
--EF BB BF
--fout:write(string.char(239, 187, 191))
local fout
position = 4
count = 0
while position do
	count = count + 1
	if count % 10000 == 1 then
		if fout then
			fout:write(tail)
			fout:close()
		end
		fout = assert(io.open("output_"..math.floor(count / 1000)..".js", "wb+"))
		if math.floor(count / 1000) == 0 then
			fout:write(head)
		else
			fout:write(head2)
		end
	end
	local oldp = position
	position = s:find(string.char(13, 10), oldp)
	if position then
		--print(oldp, position - 1)
		--print(s:sub(oldp, position - 1))
		fout:write("db.wordsearch.insert({word:'")
		local s2 = s:sub(oldp, position - 1)
		fout:write(s2:gsub("\'", "\\'"))
		fout:write("'});")
		fout:write(string.char(13, 10))
		position = position + 2
	else
		fout:write(tail)
		break
	end
end

fin:close()
if fout then fout:close() end

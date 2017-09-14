-- batch set limiter rule
local key = 'limiter-rules@%s?%s'

for i=1, table.maxn(KEYS) do 
  redis.call('hset', key, KEYS[i], ARGV[i])
end

if redis.call('EXISTS', key) == 1 then
  return true
end

return false
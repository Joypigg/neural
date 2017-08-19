-- batch set rate limiter rule
local key = 'rate_limiter_rule:'..ARGV[1]

for i=1, table.maxn(KEYS) do 
  redis.call('hset', key, KEYS[i], ARGV[i+1])
end

if redis.call('EXISTS', key) == 1 then
  return true
end

return false
-- batch set rate limiter rule
-- 获取redis机器上当前的毫秒级时间戳
local function get_now_time ()
  local now_time_array = redis.call('time')
  return now_time_array[1] * 1000 + math.floor(now_time_array[2] / 1000 + 0.5);
end

local beans = {get_now_time()};
local pattern_keyword = 'limiter-rules@*?*'..ARGV[1].."*"
-- table.insert(beans, pattern_keyword)
local keyword_table = redis.call('keys', pattern_keyword)
for i1, v1 in ipairs(keyword_table) do
  local temp_granularity_table = redis.call('hgetAll', v1)
  local rule_key = string.sub(v1, string.find(v1, '?')+1)
  
  local bean = {};
  for i2,v2 in pairs(temp_granularity_table) do
    if i2%2 == 1 then
      local incr_key = string.gsub(v1, 'rules', 'cluster', 15)..':'..v2
      local incr_value = 0
      if redis.call('EXISTS', incr_key) == 1 then
        incr_value = redis.call('get', incr_key)
      end
      table.insert(bean, {v2, temp_granularity_table[i2+1], incr_value})
    end
  end 
  table.insert(beans, rule_key)
  table.insert(beans, bean)
  table.insert(beans, string.sub(v1,string.find(v1, '@')+1,string.find(v1, '?')-1))
end

return beans
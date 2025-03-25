--- 0 no setting
--- -1 failed
--- 1 success
--- @param key flag
--- @param permits  quantity
--- @param curr_mill_second time

local function acquire(key, permits, curr_mill_second)
    local rate_limit_info = redis.pcall("HMGET", key, "last_mill_second", "curr_permits", "max_permits", "rate")
    local last_mill_second = rate_limit_info[1]
    local curr_permits = tonumber(rate_limit_info[2])
    local max_permits = tonumber(rate_limit_info[3])
    local rate = rate_limit_info[4]

    local local_curr_permits = max_permits;

    if (type(last_mill_second) ~= 'boolean' and last_mill_second ~= nil) then
        local reverse_permits = math.floor(((curr_mill_second - last_mill_second) / 1000) * rate)
        local expect_curr_permits = reverse_permits + curr_permits;
        local_curr_permits = math.min(expect_curr_permits, max_permits);

        if (reverse_permits > 0) then
            redis.pcall("HSET", key, "last_mill_second", curr_mill_second)
        end
    else
        redis.pcall("HSET", key, "last_mill_second", curr_mill_second)
    end

    local result = -1
    if (local_curr_permits - permits >= 0) then
        result = 1
        redis.pcall("HSET", key, "curr_permits", local_curr_permits - permits)
    else
        redis.pcall("HSET", key, "curr_permits", local_curr_permits)
    end

    return result
end
--eg
-- /usr/local/redis/bin/redis-cli  --eval   /work/develop/LuaDemoProject/src/luaScript/redis/rate_limiter.lua key , acquire 1  1



local function init(key, max_permits, rate)
    local rate_limit_info = redis.pcall("HMGET", key, "last_mill_second", "curr_permits", "max_permits", "rate")
    local org_max_permits = tonumber(rate_limit_info[3])
    local org_rate = rate_limit_info[4]

    if (org_max_permits == nil) or (rate ~= org_rate or max_permits ~= org_max_permits) then
        redis.pcall("HMSET", key, "max_permits", max_permits, "rate", rate, "curr_permits", max_permits)
    end
    return 1;
end
--eg
-- /usr/local/redis/bin/redis-cli  --eval   /work/develop/LuaDemoProject/src/luaScript/redis/rate_limiter.lua key , init 1  1

local function delete(key)
    redis.pcall("DEL", key)
    return 1;
end
--eg
-- /usr/local/redis/bin/redis-cli  --eval   /work/develop/LuaDemoProject/src/luaScript/redis/rate_limiter.lua key , delete



local key = KEYS[1]
local method = ARGV[1]

if method == 'acquire' then
    return acquire(key, ARGV[2], ARGV[3])
elseif method == 'init' then
    return init(key, ARGV[2], ARGV[3])
elseif method == 'delete' then
    return delete(key)
else
    --ignore
end


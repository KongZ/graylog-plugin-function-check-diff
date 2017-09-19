# Overview
A Graylog's plugin compare given text with message result from query.

For example, you want to know that are files updated from yesterday? You may use `ls dir > dir.log` command to list all files in target directory to `out.txt` and use rsyslog to send this log file to Graylog. Then you can configure Graylog Pipeline to compare a message from `dir.log` with previous message which was collected yesterday.

Rule Example:

```
rule "check graylog hash"
when
    // Your directory is started with README.me. You should prefix your log with some message that very unique.
    regex("^README.me\s.+", to_string($message.message)).matches == true
then
	// check_diff(stream_id, query, actual_message, from_date, to_date)
    let result = check_diff("000000000000000000000001", "README.me", to_string($message.message), flex_parse_date("1 day ago"), flex_parse_date("now"));
    set_field("conflict", result.conflict);
    set_field("matches", result.matches);
    set_field("expected", result.expectedResult);
end
```

If new message does not match with previous message, it will set a field `matches` to `false`
If previous message is found, and it does not match with previous message, it will set a field `conflict` to `true`
If previous message is not found, it will set a field `conflict` to `false`


![screenshot](https://github.com/omise/graylog-plugin-function-check-diff/blob/master/screenshot.png)

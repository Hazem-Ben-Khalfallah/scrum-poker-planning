"use strict";
var cards = {
    time: [
        {
            id: "time-1h",
            value: 1,
            unit: 'h',
            unitFull: 'hour',
            color: 'blue',
            index: 0
        },
        {
            id: "time-2h",
            value: 2,
            unit: 'h',
            unitFull: 'hours',
            color: 'blue',
            index: 1
        },
        {
            id: "time-4h",
            value: 4,
            unit: 'h',
            unitFull: 'hours',
            color: 'blue',
            index: 2
        },
        {
            id: "time-1d",
            value: 8,
            unit: 'h',
            unitFull: 'day',
            color: 'green',
            index: 3
        },
        {
            id: "time-1.5d",
            value: 12,
            unit: 'h',
            unitFull: 'day',
            color: 'green',
            index: 4
        },
        {
            id: "time-2d",
            value: 16,
            unit: 'h',
            unitFull: 'days',
            color: 'green',
            index: 5
        },
        {
            id: "time-3d",
            value: 24,
            unit: 'h',
            unitFull: 'days',
            color: 'green',
            index: 6
        },
        {
            id: "time-4d",
            value: 32,
            unit: 'h',
            unitFull: 'days',
            color: 'green',
            index: 7
        },
        {
            id: "time-1w",
            value: 40,
            unit: 'h',
            unitFull: 'week',
            color: 'orange',
            index: 8
        },
        {
            id: "time-2w",
            value: 80,
            unit: 'h',
            unitFull: 'weeks',
            color: 'orange',
            index: 9
        },
        {
            id: "pass",
            value: 'pass',
            color: 'grey',
            skip: true,
            index: 10
        },
        {
            id: "break",
            value: 'break',
            color: 'grey',
            skip: true,
            index: 11
        }
    ],
    fibonacci: [
        {
            id: "fib-0",
            value: 0,
            color: 'blue',
            index: 0
        },
        {
            id: "fib-1",
            value: 1,
            color: 'blue',
            index: 1
        },
        {
            id: "fib-2",
            value: 2,
            color: 'blue',
            index: 2
        },
        {
            id: "fib-3",
            value: 3,
            color: 'blue',
            index: 3
        },
        {
            id: "fib-5",
            value: 5,
            color: 'green',
            index: 4
        },
        {
            id: "fib-8",
            value: 8,
            color: 'green',
            index: 5
        },
        {
            id: "fib-13",
            value: 13,
            color: 'green',
            index: 6
        },
        {
            id: "fib-31",
            value: 21,
            color: 'green',
            index: 7
        },
        {
            id: "fib-34",
            value: 34,
            color: 'orange',
            index: 8
        },
        {
            id: "fib-55",
            value: 55,
            color: 'orange',
            index: 9
        },
        {
            id: "pass",
            value: 'pass',
            color: 'grey',
            skip: true,
            index: 10
        },
        {
            id: "break",
            value: 'break',
            color: 'grey',
            skip: true,
            index: 11
        }
    ],
    vote: [
        {
            id: "vote-0",
            value: "Yes",
            color: 'green',
            index: 5
        },
        {
            id: "vote-1",
            value: "Hold",
            color: 'yellow',
            index: 7
        },
        {
            id: "vote-2",
            value: "No",
            color: 'red',
            index: 11
        }
    ],
    modifiedFibonacci: [
        {
            id: "mfib-0",
            value: 0,
            color: 'blue',
            index: 0
        },
        {
            id: "mfib-1/2",
            value: 1 / 2,
            color: 'blue',
            index: 1
        },
        {
            id: "mfib-1",
            value: 1,
            color: 'blue',
            index: 2
        },
        {
            id: "mfib-2",
            value: 2,
            color: 'blue',
            index: 3
        },
        {
            id: "mfib-3",
            value: 3,
            color: 'blue',
            index: 4
        },
        {
            id: "mfib-5",
            value: 5,
            color: 'green',
            index: 5
        },
        {
            id: "mfib-8",
            value: 8,
            color: 'green',
            index: 6
        },
        {
            id: "mfib-13",
            value: 13,
            color: 'green',
            index: 7
        },
        {
            id: "mfib-20",
            value: 20,
            color: 'green',
            index: 8
        },
        {
            id: "mfib-40",
            value: 40,
            color: 'orange',
            index: 9
        },
        {
            id: "mfib-100",
            value: 100,
            color: 'orange',
            index: 10
        },
        {
            id: "pass",
            value: 'pass',
            color: 'grey',
            skip: true,
            index: 11
        },
        {
            id: "break",
            value: 'break',
            color: 'grey',
            skip: true,
            index: 12
        }
    ]
};
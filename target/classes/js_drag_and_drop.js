function simulateDragDrop(sourceNode, destinationNode) {
    var EVENT_TYPES = {
        MOUSE_DOWN: 'mousedown',
        MOUSE_UP: 'mouseup',
        DRAG: 'drag',
        DRAG_END: 'dragend',
        DRAG_OVER: 'dragover',
        DRAG_START: 'dragstart',
        DROP: 'drop'
    }

    function createCustomEvent(type) {
        var event = new CustomEvent("CustomEvent")
        event.initCustomEvent(type, true, true, null)
        event.dataTransfer = {
            dropEffect: 'move',
            effectAllowed: 'all',
            files :[],
            items: [],
            types: [],
            data: {
            },
            setData: function(type, val) {
                this.data[type] = val
                this.items.push(val);
                this.types.push(type);
            },
            getData: function(type) {
                return this.data[type]
            }
        }
        return event
    }

    function dispatchEvent(node, type, event) {
        if (node.dispatchEvent) {
            return node.dispatchEvent(event)
        }
        if (node.fireEvent) {
            return node.fireEvent("on" + type, event)
        }
    }


    var mouse_down = createCustomEvent(EVENT_TYPES.MOUSE_DOWN)
    dispatchEvent(sourceNode, EVENT_TYPES.MOUSE_DOWN, mouse_down)

    var event = createCustomEvent(EVENT_TYPES.DRAG_START)
    dispatchEvent(sourceNode, EVENT_TYPES.DRAG_START, event)

    var drag = createCustomEvent(EVENT_TYPES.DRAG)
    dispatchEvent(sourceNode, EVENT_TYPES.DRAG, drag)

    drag_over = createCustomEvent(EVENT_TYPES.DRAG_OVER)
    drag_over.dataTransfer = event.dataTransfer
    dispatchEvent(destinationNode, EVENT_TYPES.DRAG_OVER, drag_over)

    var dropEvent = createCustomEvent(EVENT_TYPES.DROP)
    dropEvent.dataTransfer = event.dataTransfer
    dispatchEvent(destinationNode, EVENT_TYPES.DROP, dropEvent)

    var dragEndEvent = createCustomEvent(EVENT_TYPES.DRAG_END)
    dragEndEvent.dataTransfer = event.dataTransfer
    dispatchEvent(sourceNode, EVENT_TYPES.DRAG_END, dragEndEvent)

    var mouse_up = createCustomEvent(EVENT_TYPES.MOUSE_UP)
    dispatchEvent(sourceNode, EVENT_TYPES.MOUSE_UP, mouse_up)
    return 1
}
// simulateDragDrop(document.querySelector(arguments[0]),document.querySelector(arguments[1]))
simulateDragDrop(arguments[0],arguments[1])
#@# Lame
define(['integration/random'], (Random) ->
  class Shufflerator #@# Maybe use my `Iterator` implementation, or maybe Mori has one?
    constructor: (@agents) ->
      @agents = @agents[..]
      @fetch()
    i: 0
    nextOne: null
    hasNext: -> @nextOne?
    next: ->
      result = @nextOne
      @fetch()
      result
    fetch: ->
      if @i >= @agents.length
        @nextOne = null
      else
        if @i < @agents.length - 1
          randNum = @i + Random.nextInt(@agents.length - @i)
          @nextOne = @agents[randNum]
          @agents[randNum] = @agents[@i]
        else
          @nextOne = @agents[@i]
        @i = @i + 1 #@# It's called "@i++"
      return
)

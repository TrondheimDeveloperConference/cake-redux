angular.module('cakeReduxModule')
    .controller('DraftReportCtrl', ['$scope', '$http', '$routeParams', 'eventFactory',
        function($scope, $http, $routeParams,eventFactory) {
            eventFactory(function(events) {
                if ($routeParams.eventSlug) {
                    var chosenEvent = _.findWhere(events,{slug: $routeParams.eventSlug});
                    var eventId = chosenEvent.ref;
                    if (eventId) {
                        $http({method: "GET", url: "data/draftemails?eventId=" + eventId})
                            .success(function (draftSubmitters) {
                                $scope.draftSubmitters = draftSubmitters;

                            });
                        $scope.sendEmail = function() {
                            $http({method: "POST", url: "data/emailDraftSubmitters?eventId=" + eventId})

                        }

                    }
                }
            });
        }]);


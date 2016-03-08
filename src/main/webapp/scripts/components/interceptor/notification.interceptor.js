 'use strict';

angular.module('volunteercrowdApp')
    .factory('notificationInterceptor', function ($q, AlertService) {
        return {
            response: function(response) {
                var alertKey = response.headers('X-volunteercrowdApp-alert');
                if (angular.isString(alertKey)) {
                    AlertService.success(alertKey, { param : response.headers('X-volunteercrowdApp-params')});
                }
                return response;
            }
        };
    });
